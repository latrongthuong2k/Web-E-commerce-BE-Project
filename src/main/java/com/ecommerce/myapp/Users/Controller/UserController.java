package com.ecommerce.myapp.Users.Controller;

import com.ecommerce.myapp.DTO.Category.ReqCreateCategory;
import com.ecommerce.myapp.Users.Dto.AppUserDto;
import com.ecommerce.myapp.Users.Dto.ResListUsers;
import com.ecommerce.myapp.Users.Service.UserService;
import com.ecommerce.myapp.Users.security.ReqResSecurity.ChangePasswordRequest;
import jakarta.validation.Valid;
import org.springframework.cache.annotation.Cacheable;
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
    // user list for admin web
//    @Cacheable(value = "userPage", key = "{#page,#sortField,#sortDir,#query}")
    @GetMapping("/page")
    public ResponseEntity<Map<String, Object>> getUserPage(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size,
            @RequestParam(name = "q", defaultValue = "") String query,
            @RequestParam(name = "sortField", defaultValue = "firstName") String sortField,
            @RequestParam(name = "sortDir", defaultValue = "asc") String sortDir) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDir.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortField));
        Page<ResListUsers> users = userService.getUserPage(query, pageable);
        Map<String, Object> response = new HashMap<>();
//        response.put("currentPage", productPage.getNumber());
//        response.put("totalItems", productPage.getTotalElements());
        response.put("users", users.getContent());
        response.put("totalPages", users.getTotalPages());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateUser( @RequestParam("userId") Integer userId,
                                              @Valid @RequestBody AppUserDto appUserDto) {
        userService.updateUser(userId, appUserDto);
        return ResponseEntity.ok("User updated successfully");
    }
    @PutMapping("/update-status")
    public ResponseEntity<String> updateUser( @RequestParam("userId") Integer userId) {
        userService.updateStatus(userId);
        return ResponseEntity.ok("User status updated successfully");
    }

//    @DeleteMapping("/delete")
//    public ResponseEntity<String> deleteUser(@RequestParam("userId") Integer userId) {
//        userService.deleteById(userId);
//        return ResponseEntity.ok().body("User deleted successfully");
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
