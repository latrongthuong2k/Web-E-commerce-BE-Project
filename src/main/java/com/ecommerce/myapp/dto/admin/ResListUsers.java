package com.ecommerce.myapp.dto.admin;


import java.time.LocalDateTime;

public record ResListUsers(
        Integer id,
        String firstName,
        String lastName,
        String email,
        LocalDateTime createdAt,
        LocalDateTime updateAt,
        Boolean status,
        String role
) {
}
