package com.ecommerce.myapp.Users.Audit;

import com.ecommerce.myapp.Users.Entity.AppUser;
import com.ecommerce.myapp.Users.Repository.AppUserRepository;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Optional;


public class ApplicationAuditAware implements AuditorAware<Integer> {

    private final AppUserRepository appUserRepository;

    public ApplicationAuditAware(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Override
    public Optional<Integer> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() ||
            authentication instanceof AnonymousAuthenticationToken) {
            return Optional.empty();
        }

        // Kiểm tra loại của principal trước khi ép kiểu
        if (authentication.getPrincipal() instanceof OAuth2User) {
            OAuth2User userPrincipal = (OAuth2User) authentication.getPrincipal();
            String email = (String) userPrincipal.getAttributes().get("email");

            // Lấy ra user từ cơ sở dữ liệu dựa trên email
            Optional<AppUser> appUserOptional = appUserRepository.findByEmail(email);

            // Trả về id của user nếu có, nếu không trả về Optional.empty()
            return appUserOptional.map(AppUser::getId);
        } else if (authentication.getPrincipal() instanceof AppUser) {
            // Nếu principal là AppUser, xử lý tương tự
            AppUser appUser = (AppUser) authentication.getPrincipal();
            return Optional.of(appUser.getId());
        }

        return Optional.empty();
    }
}
