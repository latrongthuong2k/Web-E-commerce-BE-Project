package com.ecommerce.myapp.DTO.Bill;

import com.ecommerce.myapp.Entity.Bill.Purchases;
import com.ecommerce.myapp.Entity.Bill.Sales;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ReqCreateBill(
        @NotNull(message = "User id can't be null") Integer userId,
        List<Purchases> purchases,
        List<Sales> sales
        ) {
}
