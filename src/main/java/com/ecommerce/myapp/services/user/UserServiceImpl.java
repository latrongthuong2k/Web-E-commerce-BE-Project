package com.ecommerce.myapp.services.user;

import com.ecommerce.myapp.dto.Mapper.AppUserMapper;
import com.ecommerce.myapp.dto.admin.*;
import com.ecommerce.myapp.exceptions.ResourceNotFoundException;
import com.ecommerce.myapp.model.user.AppUser;
import com.ecommerce.myapp.repositories.AppUserRepository;
import com.ecommerce.myapp.repositories.UserImageRepository;
import com.ecommerce.myapp.s3.S3Buckets;
import com.ecommerce.myapp.s3.S3Service;
import com.ecommerce.myapp.security.Auth.AuthenticationService;
import com.ecommerce.myapp.security.ReqResSecurity.ChangePasswordRequest;
import com.ecommerce.myapp.security.ReqResSecurity.ChangeRoleRequest;
import com.ecommerce.myapp.security.Token.Token;
import com.ecommerce.myapp.security.Token.TokenRepository;
import com.ecommerce.myapp.validation.EntityExistenceValidatorService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@Transactional(rollbackOn = Exception.class)
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final AppUserRepository appUserRepository;
    private final AppUserMapper appUserMapper;
    private final PasswordEncoder passwordEncoder;
    private final TokenRepository tokenRepository;
    private final UserImageRepository userImageRepository;
    private final EntityExistenceValidatorService validatorService;
    private final UserPageMapper userPageMapper;
    private final AuthenticationService authenticationService;
    private final S3Service s3Service;
    private final S3Buckets s3Buckets;
    private final CacheManager cacheManager;
    // Auth
    public void changePassword(ChangePasswordRequest request, Principal connectedUser) {

        var user = (AppUser) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        validateChangePasswordReq(request, user);
    }

    public void adminChangePasswordToAnother(ChangePasswordRequest request, AppUser user) {
        validateChangePasswordReq(request, user);
    }

    private void validateChangePasswordReq(ChangePasswordRequest request, AppUser user) {
        // check if the current password is correct
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Wrong password");
        }
        // check if the two new passwords are the same
        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw new IllegalArgumentException("Password are not the same");
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
    public void saveUser(AppUserDto appUserDto) {
        AppUser appUser = appUserMapper.toEntity(appUserDto);
        appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
        AppUser savedUser = appUserRepository.save(appUser);
        authenticationService.adminCreate(savedUser);
    }

    @Override
    public ResUserPrevData getUserPrevDataById(Integer id) {
        AppUser appUser = foundUser(id);
        return new ResUserPrevData(appUser.getFirstName(),
                appUser.getLastName(),
                appUser.getEmail());
    }


    @CacheEvict(value = "user", key = "#userId") // xoá cache
    @Override
    public void updateUser(Integer userId, ChangeDetail changeDetail) {
        AppUser appUser = foundUser(userId);
        appUser.setFirstName(changeDetail.firstName());
        appUser.setLastName(changeDetail.lastName());
        appUser.setEmail(changeDetail.changeEmailPassWordReq().getNewEmail());
        adminChangePasswordToAnother(changeDetail.changeEmailPassWordReq().getChangePasswordRequest(), appUser);
        appUserRepository.save(appUser);
    }

    @Override
    public List<AppUserDto> findAll() {
        List<AppUser> appUsers = appUserRepository.findAll();
        return appUsers.stream().map(appUserMapper::toDto).collect(Collectors.toList());
    }


    // chỉ có Admin role mới xoá được
    @Override
    @CacheEvict(value = "user", key = "#userId") // xoá cache
    public void deleteById(Integer userId) {
        validatorService.checkExistUser(userId);
        // clean token
        tokenRepository.deleteByUserId(userId);
        appUserRepository.deleteById(userId);
    }

    @Override
//    @CacheEvict(value = "userId",allEntries = true) // thêm cache
    public AppUser findById(Integer userId) {
        if (userId == null) {
            return null;
        }
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

    @Override
    public Page<ResListUsers> getAdminPage(String query, Pageable pageable) {
        Page<AppUser> users = appUserRepository
                .findBySearch(query, pageable);
        return users.map(userPageMapper::toDto);
    }

    @Override
    public void updateStatus(Integer userId) {
        AppUser appUser = foundUser(userId);
        boolean currentStatus = appUser.getStatus();
        appUser.setStatus(!currentStatus);
    }


    //-------------
    private AppUser foundUser(Integer userId) {
        return appUserRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User with id [%s] is not found ".formatted(userId)));
    }

    private Optional<AppUser> foundOptionalUser(Integer userId) {
        return appUserRepository.findById(userId);
    }
}