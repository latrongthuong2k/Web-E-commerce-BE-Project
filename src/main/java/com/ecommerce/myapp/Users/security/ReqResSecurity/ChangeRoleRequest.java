package com.ecommerce.myapp.Users.security.ReqResSecurity;

import com.ecommerce.myapp.Users.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeRoleRequest {
    private Role role;
}
