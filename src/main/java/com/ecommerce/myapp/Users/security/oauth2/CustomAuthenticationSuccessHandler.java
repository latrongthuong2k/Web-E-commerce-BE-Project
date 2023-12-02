package com.ecommerce.myapp.Users.security.oauth2;

import com.ecommerce.myapp.Users.Entity.AppUser;
import com.ecommerce.myapp.Users.Repository.AppUserRepository;
import com.ecommerce.myapp.Users.Role;
import com.ecommerce.myapp.Users.security.ReqResSecurity.AuthenticationResponse;
import com.ecommerce.myapp.Users.security.Token.Token;
import com.ecommerce.myapp.Users.security.Token.TokenRepository;
import com.ecommerce.myapp.Users.security.Token.TokenType;
import com.ecommerce.myapp.Users.security.config.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;

@Component
@AllArgsConstructor
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final RandomPasswordGenerator randomPasswordGenerator;
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;


    public AuthenticationResponse oAuth2Register(AppUser user) {
        var jwtToken = jwtService.generateToken(user);
        saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .build();
    }

    public AuthenticationResponse oAuth2Authenticate(AppUser user) {
        var refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, refreshToken);
        return AuthenticationResponse.builder()
                .accessToken(refreshToken)
                .build();
    }

    private void saveUserToken(AppUser user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(AppUser user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String email = (String) attributes.get("email");
        String givenName = (String) attributes.get("given_name");
        String name = (String) attributes.get("name");
        // Check if the user exists, and if not create a new one
        AppUser user;
        AuthenticationResponse authRes;
        // Check if the user exists, and if not create a new one
        Optional<AppUser> existingUser = appUserRepository.findByEmail(email);
        if (existingUser.isPresent()) {
            user = existingUser.get();
            authRes = oAuth2Authenticate(user);
        } else {
            // User doesn't exist, create a new one
            user = new AppUser();
            // Set fields from attributes
            user.setFirstName(givenName);
            user.setLastName(name);
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode(randomPasswordGenerator.generateRandomPassword(10)));
            user.setRole(Role.USER);
            appUserRepository.save(user);
            authRes = oAuth2Register(user);
        }

        // Set content type to JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        Cookie tokenCookie = new Cookie("auth-token", URLEncoder.encode(authRes.getAccessToken(), StandardCharsets.UTF_8));
        tokenCookie.setHttpOnly(true);
        tokenCookie.setSecure(true);
        tokenCookie.setPath("/");
        tokenCookie.setMaxAge(7 * 24 * 60 * 60);
        response.addCookie(tokenCookie);
        // Redirect without token in the URL
        response.sendRedirect("http://localhost:3000/pages/dashboard");


        // Send token as a JSON response
//         getRedirectStrategy().sendRedirect(request, response,
//                "http://localhost:3000/api?token=" +
//                URLEncoder.encode(authRes.getAccessToken() , StandardCharsets.UTF_8));
    }
}