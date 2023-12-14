package com.ecommerce.myapp.services.app;

import com.ecommerce.myapp.model.user.AppUser;
import com.ecommerce.myapp.repositories.AppUserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional(rollbackOn = Exception.class)
@RequiredArgsConstructor
public class AppAuditAwareService implements AuditorAware<Integer> {

    private final AppUserRepository appUserRepository;

    @Override
    public Optional<Integer> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() ||
            authentication instanceof AnonymousAuthenticationToken) {
            return Optional.empty();
        }

        // Kiểm tra loại của principal trước khi ép kiểu
        if (authentication.getPrincipal() instanceof OAuth2User userPrincipal) {
            String email = (String) userPrincipal.getAttributes().get("email");

            // Lấy ra user từ cơ sở dữ liệu dựa trên email
            Optional<AppUser> appUserOptional = appUserRepository.findByEmail(email);

            // Trả về id của user nếu có, nếu không trả về Optional.empty()
            return appUserOptional.map(AppUser::getId);
        } else if (authentication.getPrincipal() instanceof AppUser appUser) {
            // Nếu principal là AppUser, xử lý tương tự
            return Optional.of(appUser.getId());
        }

        return Optional.empty();
    }
}
