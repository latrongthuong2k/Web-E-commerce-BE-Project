package com.ecommerce.myapp.Users.Controller;

import com.ecommerce.myapp.Users.Dto.AppUserDto;
import com.ecommerce.myapp.Users.Dto.ChangeDetail;
import com.ecommerce.myapp.Users.Dto.ResListUsers;
import com.ecommerce.myapp.Users.Dto.ResUserPrevData;
import com.ecommerce.myapp.Users.Service.UserService;
import com.ecommerce.myapp.Users.security.ReqResSecurity.ChangeEmailPassWordReq;
import com.ecommerce.myapp.Users.security.Token.TokenRepository;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Hidden
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final TokenRepository tokenRepository;
    private final UserService userService;

    /**
     * Quản lý toàn bộ hệ thống: cấu hình, bảo trì, và nâng cấp ứng dụng.
     * Quản lý người dùng, phân quyền, và xử lý các vấn đề kỹ thuật.
     * Giám sát và phân tích hoạt động trang web để cải thiện trải nghiệm người dùng và hiệu suất kinh doanh.
     */

    @GetMapping("/get")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<ResUserPrevData> get(@RequestParam("userId") Integer userId) {
        return ResponseEntity.ok(userService.getUserPrevDataById(userId));
    }

    @GetMapping("/users")
    public ResponseEntity<List<AppUserDto>> getAllUser() {
        List<AppUserDto> users = userService.findAll();
        return ResponseEntity.ok(users);
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateUser( @RequestParam("userId") Integer userId,
                                              @Valid @RequestBody ChangeDetail changeDetail) {
        userService.updateUser(userId, changeDetail);
        return ResponseEntity.ok("User updated successfully");
    }
    @PostMapping
    @PreAuthorize("hasAuthority('admin:create')")
    public ResponseEntity<String> createAdmin(@Valid @RequestBody AppUserDto newUser) {
        userService.saveUser(newUser);
        return ResponseEntity.ok("Admin account is created successfully");
    }

    // force cleanup-tokens
    @PostMapping("/cleanup-tokens")
    public void cleanupTokens() {
        tokenRepository.deleteExpiredTokens();
    }

    // user list for admin web
    //@Cacheable(value = "userPage", key = "{#page,#sortField,#sortDir,#query}")
    @GetMapping("/page")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<Map<String, Object>> getAdminPage(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size,
            @RequestParam(name = "q", defaultValue = "") String query,
            @RequestParam(name = "sortField", defaultValue = "firstName") String sortField,
            @RequestParam(name = "sortDir", defaultValue = "asc") String sortDir) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDir.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortField));
        Page<ResListUsers> adminPage = userService.getAdminPage(query, pageable);
        Map<String, Object> response = new HashMap<>();
//        response.put("currentPage", productPage.getNumber());
//        response.put("totalItems", productPage.getTotalElements());
        response.put("users", adminPage.getContent());
        response.put("totalPages", adminPage.getTotalPages());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update-status")
    public ResponseEntity<String> updateUser(@RequestParam("userId") Integer userId) {
        userService.updateStatus(userId);
        return ResponseEntity.ok("Status of User with role (ADMIN)  updated successfully");
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasAuthority('admin:delete')")
    public ResponseEntity<String> deleteUser(@RequestParam("userId") Integer userId) {
        userService.deleteById(userId);
        return ResponseEntity.ok().body("User with role (ADMIN) is deleted successfully");
    }
//    @PutMapping
//    @PreAuthorize("hasAuthority('admin:update')")
//    public String put() {
//        return "PUT:: admin controller";
//    }
//    @DeleteMapping
//    @PreAuthorize("hasAuthority('admin:delete')")
//    public String delete() {
//
//
//
//        return "DELETE:: admin controller";
//    }
}