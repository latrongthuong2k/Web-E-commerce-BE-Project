package com.ecommerce.myapp.Users.Controller;

import com.ecommerce.myapp.Users.security.Token.TokenRepository;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Hidden
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final TokenRepository tokenRepository;
    /**
     * Quản lý toàn bộ hệ thống: cấu hình, bảo trì, và nâng cấp ứng dụng.
     * Quản lý người dùng, phân quyền, và xử lý các vấn đề kỹ thuật.
     * Giám sát và phân tích hoạt động trang web để cải thiện trải nghiệm người dùng và hiệu suất kinh doanh.
     */

    @GetMapping("/take")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<String> get() {
        System.out.println("haha");
        return ResponseEntity.ok("ok");
    }
//
//
//    @PostMapping
//    @PreAuthorize("hasAuthority('admin:create')")
//    public String post() {
//        return "POST:: admin controller";
//    }
//
    // force cleanup-tokens
    @PostMapping("/cleanup-tokens")
    public void cleanupTokens() {
        tokenRepository.deleteExpiredTokens();
    }
//
//
//
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