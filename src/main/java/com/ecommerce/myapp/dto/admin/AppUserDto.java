package com.ecommerce.myapp.dto.admin;

import com.ecommerce.myapp.model.user.AppUser;
import jakarta.validation.constraints.*;

import java.io.Serializable;

/**
 * DTO for {@link AppUser}
 */
public record AppUserDto(

        @Size(message = "maximum limit character for first name is 30",max = 30)
        @NotBlank(message = "first name cannot be blank")
        String firstName,
        //
        @Size(message = "maximum limit character for last name is 30",max = 30)
        @NotBlank(message = "last name cannot be blank")
        String lastName,
        //
        @Size(message = "maximum limit character for email is 50", max = 50)
        @Email(message = "Wrong email format")
        @NotBlank(message = "Username cannot be blank")
        String email,
        //
        @Size(message = "maximum limit character for password is 50",max = 50)
        @Pattern(message = "Password must have a capital letter and at least one special character",
                regexp = "^(?=.*[A-Z])(?=.*[@#$%^&+=!]).+$")
        @NotBlank(message = "password cannot be blank")
        String password,

        @NotNull
        String role

) implements Serializable {
}