package com.ecommerce.myapp.Users.Dto;

import jakarta.validation.constraints.NotNull;

public record UpdateStatusDto(@NotNull Boolean status) {
}
