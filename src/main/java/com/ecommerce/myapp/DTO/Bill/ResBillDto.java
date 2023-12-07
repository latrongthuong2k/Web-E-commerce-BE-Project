package com.ecommerce.myapp.DTO.Bill;

import com.ecommerce.myapp.Entity.Bill.BillStatus;

public record ResBillDto(
        Integer BillId,
        String customerEmail,
        BillStatus status
) {
}
