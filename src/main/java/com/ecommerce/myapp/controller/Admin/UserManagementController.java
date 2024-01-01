package com.ecommerce.myapp.controller.Admin;

import com.ecommerce.myapp.dtos.user.response.ResListUsers;
import com.ecommerce.myapp.dtos.user.response.ResUserDetailData;
import com.ecommerce.myapp.dtos.user.response.UserPublicDataMapper;
import com.ecommerce.myapp.model.user.Role;
import com.ecommerce.myapp.services.UserService;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/management/user")
@PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
public class UserManagementController {

    private final UserService userService;
    private final UserPublicDataMapper userPublicDataMapper;

    private Map<String, Object> objectMap(Page<ResListUsers> productPage) {
        Map<String, Object> response = new HashMap<>();
        response.put("content", productPage.getContent());
        response.put("totalPages", productPage.getTotalPages());
        return response;
    }

    @Cacheable(value = "user-table", key = "{#page,#sortField,#sortDir,#query}",
            condition = "#query.length() > 3")
    @GetMapping("/user-table")
    public ResponseEntity<Map<String, Object>> getUserAccounts(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size,
            @RequestParam(name = "q", defaultValue = "") String query,
            @RequestParam(name = "sortField", defaultValue = "fullName") String sortField,
            @RequestParam(name = "sortDir", defaultValue = "asc") String sortDir) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDir.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortField));
        Page<ResListUsers> usersPage;
        if (isAdmin) {
            usersPage = userService.getNonAdminUserAccounts(query, pageable);
        } else {
            usersPage = userService.getUserAccounts(query, pageable);
        }
        Map<String, Object> response = objectMap(usersPage);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('admin:update')")
    @PutMapping("/role")
    public ResponseEntity<Void> upgradeToAdmin(@NotNull Role role) {
        userService.changeRole(role);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @CacheEvict(value = "user-table", allEntries = true)
    @PreAuthorize("hasAuthority('admin:update')")
    @PutMapping("/user-status")
    public ResponseEntity<Void> updateUserStatus(@RequestParam("userId") Long userId) {
        userService.updateStatus(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('admin:get')")
    @GetMapping("/get-roles")
    public ResponseEntity<List<Role>> getRoles() {
        List<Role> roles = Role.ADMIN.getAllRoles();
        return ResponseEntity.ok(roles);
    }

    @GetMapping("/get-detail")
    public ResponseEntity<ResUserDetailData> getUserDetail(@RequestParam("userId") Long userId) {
        var user = userService.findById(userId);
        return ResponseEntity.ok(userPublicDataMapper.toDto(user));
    }

    //    @CacheEvict(value = "user-table", allEntries = true)
    @PreAuthorize("hasAuthority('admin:delete')")
    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteUser(@RequestParam("userId") Long userId) {
        userService.deleteById(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
