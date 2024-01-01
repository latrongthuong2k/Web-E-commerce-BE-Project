package com.ecommerce.myapp.dtos.user.request;

import com.ecommerce.myapp.model.user.AppUser;
import com.ecommerce.myapp.security.ReqResSecurity.ChangeEmailPasswordReq;
import jakarta.validation.constraints.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

/**
 * DTO for {@link AppUser}
 */
public record UserChangeInfoReq(

        @NotBlank(message = "Email cannot be blank")
        @Email(message = "Wrong email format")
        @Size(max = 255, message = "Maximum limit of characters for email is 255")
        String email,

        @NotBlank(message = "User full name can't be blank")
        String fullName,

        @NotBlank(message = "Phone number cannot be blank")
        @Size(max = 15, message = "Maximum limit of characters for phone number is 15")
        @Pattern(regexp = "^0[35789]\\d{8}$", message = "Invalid phone number")
        String phone,

        MultipartFile avatarFile,

        @NotNull(message = "Change email/password request cannot be null")
        ChangeEmailPasswordReq changeEmailPassWordReq

) implements Serializable {
}