package com.ecommerce.myapp.dtos.user.response;


import java.io.Serializable;

public record ResUserDetailData(
        String userName,
        String email,
        String fullName,
        String phone
) implements Serializable {
}
