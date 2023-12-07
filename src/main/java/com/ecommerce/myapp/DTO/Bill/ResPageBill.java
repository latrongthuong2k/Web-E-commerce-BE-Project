package com.ecommerce.myapp.DTO.Bill;

import com.ecommerce.myapp.Entity.Bill.BillStatus;
import com.ecommerce.myapp.Entity.Bill.Purchases;
import com.ecommerce.myapp.Entity.Bill.Sales;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.CreatedBy;

import java.time.LocalDateTime;
import java.util.List;

public record ResPageBill(
        Integer id,
        LocalDateTime createdDate,
        LocalDateTime approvedDate,
        String createdBy,
        String approvedBy,
        BillStatus billStatus
) {
}
