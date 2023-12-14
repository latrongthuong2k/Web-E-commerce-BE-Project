package com.ecommerce.myapp.security.ReqResSecurity;

import com.ecommerce.myapp.model.user.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeRoleRequest {
    private Role role;
}
