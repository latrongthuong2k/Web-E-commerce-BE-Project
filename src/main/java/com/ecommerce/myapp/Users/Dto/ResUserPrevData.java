package com.ecommerce.myapp.Users.Dto;

import java.io.Serializable;

public record ResUserPrevData(
        String firstName,
        String lastName,
        String email
) implements Serializable {
}
