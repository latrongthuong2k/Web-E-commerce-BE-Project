package com.ecommerce.myapp.dto.admin;

import com.ecommerce.myapp.model.user.AppUser;
import com.ecommerce.myapp.security.ReqResSecurity.ChangeEmailPassWordReq;
import jakarta.validation.constraints.*;

import java.io.Serializable;

/**
 * DTO for {@link AppUser}
 */
public record ChangeDetail(

        @Size(message = "maximum limit character for first name is 30",max = 30)
        @NotBlank(message = "first name cannot be blank")
        String firstName,
        //
        @Size(message = "maximum limit character for last name is 30",max = 30)
        @NotBlank(message = "last name cannot be blank")
        String lastName,

        ChangeEmailPassWordReq changeEmailPassWordReq

) implements Serializable {
}