package com.ecommerce.myapp.Users.security.Auth;

import com.ecommerce.myapp.Users.security.Token.TokenRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;


@RestController
@AllArgsConstructor
@RequestMapping("/api/v2/auth")
public class AuthenticationControllerV2 {

    private final TokenRepository tokenRepository;
    @GetMapping("/check-authentication")
    public ResponseEntity<?> checkAuthentication(HttpServletRequest request) throws IOException {

       String token =  getCookieByName(request, "auth-token");
        if(validateToken(token))
        {
            return ResponseEntity.ok("Authenticated");
        }else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid auth token");
        }
    }

    public static String getCookieByName(HttpServletRequest request, String nameCookie) {
        Cookie[] cookies = request.getCookies();
        String token = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (nameCookie.equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }
        return token;
    }

    private Boolean validateToken(String token) {
        return tokenRepository.findByToken(token)
                .map(t -> !t.isExpired() && !t.isRevoked())
                .orElse(false);
    }
}
