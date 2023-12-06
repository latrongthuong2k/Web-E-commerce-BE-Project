package com.ecommerce.myapp.Users.Service;


import com.ecommerce.myapp.Users.Dto.ResListUsers;
import com.ecommerce.myapp.Users.Entity.AppUser;
import com.ecommerce.myapp.Users.Dto.AppUserDto;
import com.ecommerce.myapp.Users.security.ReqResSecurity.ChangePasswordRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

public interface UserService {
    AppUser saveUser(AppUserDto user);

    void updateUserPasswordById(Integer userId, AppUserDto appUserDto);

    void updateUser(Integer userId, AppUserDto appUserDto);

    List<AppUserDto> findAll();

    void deleteById(Integer id);
    AppUser findById(Integer userId);

    void changePassword(ChangePasswordRequest request, Principal connectedUser);

    boolean existsByEmail(String email);

    void uploadUserProfileImage(Integer userId,
                                    MultipartFile file);

    byte[] getUserProfileImage(Integer userId);

    Page<ResListUsers> getAdminPage(String query, Pageable pageable);

    void updateStatus(Integer userId);
}
