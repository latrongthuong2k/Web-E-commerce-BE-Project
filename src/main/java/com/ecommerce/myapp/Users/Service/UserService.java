package com.ecommerce.myapp.Users.Service;


import com.ecommerce.myapp.Users.Dto.ChangeDetail;
import com.ecommerce.myapp.Users.Dto.ResListUsers;
import com.ecommerce.myapp.Users.Dto.ResUserPrevData;
import com.ecommerce.myapp.Users.Entity.AppUser;
import com.ecommerce.myapp.Users.Dto.AppUserDto;
import com.ecommerce.myapp.Users.security.ReqResSecurity.ChangePasswordRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

public interface UserService {
    void saveUser(AppUserDto user);

    ResUserPrevData getUserPrevDataById(Integer id);

    void updateUser(Integer userId, ChangeDetail emailPassWordReq);

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
