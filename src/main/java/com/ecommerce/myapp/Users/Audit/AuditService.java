package com.ecommerce.myapp.Users.Audit;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuditService {
    private final AuditorAware<Integer> auditorAware;

    public AuditService(AuditorAware<Integer> auditorAware) {
        this.auditorAware = auditorAware;
    }

    // lấy userId hiện tại đang thực hiện đăng nhập
    public Optional<Integer> getAuditorId(){
        return auditorAware.getCurrentAuditor();
    }

}
