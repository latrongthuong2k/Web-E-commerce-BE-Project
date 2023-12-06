package com.ecommerce.myapp.Users.security.Auth;

import com.ecommerce.myapp.Users.Service.UserService;
import com.ecommerce.myapp.Users.security.ReqResSecurity.AuthenticationRequest;
import com.ecommerce.myapp.Users.security.ReqResSecurity.RegisterRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authService;
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (userService.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body("Email has already been taken");
        }
        return ResponseEntity.ok(authService.register(request));
    }

//    @PostMapping("/authenticate")
//    public ResponseEntity<AuthenticationResponse> authenticate(
//            @RequestBody AuthenticationRequest request
//    ) {
//        return ResponseEntity.ok(authService.authenticate(request));
//    }

@PostMapping("/authenticate")
public ResponseEntity<?> authenticate(
        @RequestBody AuthenticationRequest request,
        HttpServletResponse response
) {
    authService.authenticate(request, response);
    return ResponseEntity.ok().build();
}


    @PostMapping("/refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        authService.refreshToken(request, response);
    }
}