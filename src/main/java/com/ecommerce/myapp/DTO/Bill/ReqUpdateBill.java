package com.ecommerce.myapp.DTO.Bill;

import com.ecommerce.myapp.Entity.Bill.BillStatus;
import jakarta.validation.constraints.NotNull;


public record ReqUpdateBill(
        @NotNull(message = "Email can't be null") String customerEmail,
        @NotNull(message = "status can't be null") BillStatus status) {
}
