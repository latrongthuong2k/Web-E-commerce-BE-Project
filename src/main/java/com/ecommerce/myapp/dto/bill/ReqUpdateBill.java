package com.ecommerce.myapp.dto.bill;

import com.ecommerce.myapp.model.bill.BillStatus;
import jakarta.validation.constraints.NotNull;


public record ReqUpdateBill(
        @NotNull(message = "Email can't be null") String customerEmail,
        @NotNull(message = "status can't be null") BillStatus status) {
}
