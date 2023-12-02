package com.ecommerce.myapp.Users.Controller;

import com.ecommerce.myapp.Users.HttpReqRes.AppUserDto;
import com.ecommerce.myapp.Users.Service.UserService;
import com.ecommerce.myapp.Users.security.ReqResSecurity.ChangePasswordRequest;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PatchMapping
    public ResponseEntity<?> changePassword(
            @RequestBody ChangePasswordRequest request,
            Principal connectedUser
    ) {
        userService.changePassword(request, connectedUser);
        return ResponseEntity.ok().build();
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


    @GetMapping("/users")
    public ResponseEntity<List<AppUserDto>> getAllUser() {
        List<AppUserDto> users = userService.findAll();
        return ResponseEntity.ok(users);
    }

    @PutMapping("/update/{userId}")
    public ResponseEntity<String> updateUser(@PathVariable Integer userId, @Valid @RequestBody AppUserDto appUserDto) {
        userService.updateUser(userId, appUserDto);
        return ResponseEntity.ok("User updated successfully");
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Integer userId) {
        userService.deleteById(userId);
        return ResponseEntity.ok().body("User deleted successfully");
    }

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
