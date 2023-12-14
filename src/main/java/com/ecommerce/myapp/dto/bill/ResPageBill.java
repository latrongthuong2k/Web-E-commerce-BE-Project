package com.ecommerce.myapp.dto.bill;

import com.ecommerce.myapp.model.bill.BillStatus;

import java.time.LocalDateTime;

public record ResPageBill(
        Integer id,
        LocalDateTime createdDate,
        LocalDateTime approvedDate,
        String createdBy,
        String approvedBy,
        BillStatus billStatus
) {
}
