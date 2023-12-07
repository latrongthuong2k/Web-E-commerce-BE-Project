package com.ecommerce.myapp.Users.Controller;

import com.ecommerce.myapp.Users.Dto.AppUserDto;
import com.ecommerce.myapp.Users.Dto.ResListUsers;
import com.ecommerce.myapp.Users.Service.UserService;
import com.ecommerce.myapp.Users.security.ReqResSecurity.ChangeEmailPassWordReq;
import com.ecommerce.myapp.Users.security.ReqResSecurity.ChangePasswordRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


//    Ẩn vì có phương thức tạo bên Auth rồi
//    @PostMapping("/create")
//    public ResponseEntity<String> createUser(@RequestBody AppUserDto appUserDto) {
//        AppUser newUser = userService.saveUser(appUserDto);
//        URI location = ServletUriComponentsBuilder
//                .fromCurrentRequest()
//                .path("/{userId}")
//                .buildAndExpand(newUser.getId())
//                .toUri();
//        return ResponseEntity.created(location).body("User created successfully");
//    }








    @PostMapping(
            value = "{customerId}/profile-image",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public void uploadUserProfileImage(
            @PathVariable("customerId") Integer customerId,
            @RequestParam("file") MultipartFile file) {
        userService.uploadUserProfileImage(customerId, file);
    }
    @GetMapping(
            value = "{customerId}/profile-image",
            produces = MediaType.IMAGE_JPEG_VALUE
    )
    public byte[] getUserProfileImage(
            @PathVariable("customerId") Integer customerId) {
        return userService.getUserProfileImage(customerId);
    }

}
