package com.ecommerce.myapp.Users.Service;

import com.ecommerce.myapp.Exceptions.ResourceNotFoundException;
import com.ecommerce.myapp.Services.validation.EntityExistenceValidatorService;
import com.ecommerce.myapp.Users.AppUserMapper;
import com.ecommerce.myapp.Users.Entity.AppUser;
import com.ecommerce.myapp.Users.HttpReqRes.AppUserDto;
import com.ecommerce.myapp.Users.Repository.AppUserRepository;
import com.ecommerce.myapp.Users.Repository.UserImageRepository;
import com.ecommerce.myapp.Users.security.ReqResSecurity.ChangePasswordRequest;
import com.ecommerce.myapp.Users.security.ReqResSecurity.ChangeRoleRequest;
import com.ecommerce.myapp.Users.security.Token.Token;
import com.ecommerce.myapp.Users.security.Token.TokenRepository;
import com.ecommerce.myapp.s3.S3Buckets;
import com.ecommerce.myapp.s3.S3Service;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@Transactional
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final AppUserRepository appUserRepository;
    private final AppUserMapper appUserMapper;
    private final PasswordEncoder passwordEncoder;
    private final TokenRepository tokenRepository;
    private final S3Service s3Service;
    private final S3Buckets s3Buckets;
    private final UserImageRepository userImageRepository;
    private final EntityExistenceValidatorService validatorService;

    // Auth
    public void changePassword(ChangePasswordRequest request, Principal connectedUser) {

        var user = (AppUser) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        // check if the current password is correct
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalStateException("Wrong password");
        }
        // check if the two new passwords are the same
        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw new IllegalStateException("Password are not the same");
        }
        // update the password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        // save the new password
        appUserRepository.save(user);
    }

    // Todo: gọi hàm này trong menu Worker management -> acc đã thay đổi role thì sẽ bắt đăng nhập lại để có token mới
    public void changeRole(ChangeRoleRequest request, Principal connectedUser) {
        var user = (AppUser) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        user.setRole(request.getRole());
        // save new user with new authorize
        appUserRepository.save(user);
        // setExpired and revoke token
        List<Token> jwt = tokenRepository.findAllValidTokenByUser(user.getId());
        if (!jwt.isEmpty()) {
            jwt.forEach(token -> {
                token.setExpired(true);
                token.setRevoked(true);
            });
        }
        tokenRepository.saveAll(jwt);
    }


    @Override
    public AppUser saveUser(AppUserDto appUserDto) {
        AppUser appUser = appUserMapper.toEntity(appUserDto);
        return appUserRepository.save(appUser);
    }


    @Override
    public void updateUserPasswordById(Integer userId, AppUserDto appUserDto) {
        AppUser appUser = foundUser(userId);
        appUser.setPassword(appUserDto.password());
        appUserRepository.save(appUser);
    }

    @Override
    public void updateUser(Integer userId, AppUserDto appUserDto) {
        AppUser appUser = foundUser(userId);
//        appUser.setUserName(appUserDto.userName());
        appUser.setEmail(appUserDto.email());
        appUser.setPassword(appUserDto.password());
        appUserRepository.save(appUser);
    }

    @Override
    public List<AppUserDto> findAll() {
        List<AppUser> appUsers = appUserRepository.findAll();
        return appUsers.stream().map(appUserMapper::toDto).collect(Collectors.toList());
    }


    @Override
    @CacheEvict(value = "users", key = "#userId") // xoá cache
    public void deleteById(Integer userId) {
        validatorService.checkExistUser(userId);
        appUserRepository.deleteById(userId);
}

    @Override
    @Cacheable(value = "userId", key = "#userId") // thêm cache
    public AppUser findById(Integer userId) {
        return foundUser(userId);
    }

    @Override
    public boolean existsByEmail(String email) {
        Optional<AppUser> appUser = appUserRepository.findByEmail(email);
        return appUser.isPresent();
    }

    @Override
    public void uploadUserProfileImage(Integer userId,
                                       MultipartFile file) {
        validatorService.checkExistUser(userId);
        String key = UUID.randomUUID().toString();
        try {
            s3Service.putObject(
                    s3Buckets.getProduct(), // bucketName
                    "profile-images/%s/%s".formatted(userId, key),
                    file.getBytes()
            );
        } catch (IOException e) {
            throw new RuntimeException("failed to upload profile image", e);
        }
        appUserRepository.updateProfileImageId(key, userId);
    }

    @Override
    public byte[] getUserProfileImage(Integer userId) {
        var user = appUserRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(
                "user with id [%s] not found".formatted(userId)
        ));
        if (!userImageRepository.existsByUserId(user)) {
            throw new EntityNotFoundException("User with id [%s] profile image not found".formatted(userId));
        }
        return s3Service.getObject(
                s3Buckets.getProduct(),
                "profile-images/%s/%s".formatted(userId, user.getUserImage().getKey())
        );
    }


    //-------------
    private AppUser foundUser(Integer userId) {
        return appUserRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User with id [%s] is not found : ".formatted(userId)));
    }
}