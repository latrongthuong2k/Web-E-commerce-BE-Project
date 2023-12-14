package com.ecommerce.myapp.services.user;


import com.ecommerce.myapp.dto.admin.ChangeDetail;
import com.ecommerce.myapp.dto.admin.ResListUsers;
import com.ecommerce.myapp.dto.admin.ResUserPrevData;
import com.ecommerce.myapp.model.user.AppUser;
import com.ecommerce.myapp.dto.admin.AppUserDto;
import com.ecommerce.myapp.security.ReqResSecurity.ChangePasswordRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

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
