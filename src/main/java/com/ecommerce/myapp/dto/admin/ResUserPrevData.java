package com.ecommerce.myapp.dto.admin;

import java.io.Serializable;

public record ResUserPrevData(
        String firstName,
        String lastName,
        String email
) implements Serializable {
}
