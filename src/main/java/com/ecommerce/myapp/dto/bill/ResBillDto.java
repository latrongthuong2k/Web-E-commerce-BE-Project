package com.ecommerce.myapp.dto.bill;

import com.ecommerce.myapp.model.bill.BillStatus;

public record ResBillDto(
        Integer BillId,
        String customerEmail,
        BillStatus status
) {
}
