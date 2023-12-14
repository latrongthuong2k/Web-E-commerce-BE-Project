package com.ecommerce.myapp.dto.bill;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ReqCreateBill(
        @NotNull(message = "User id can't be null") Integer userId,
        List<ReqCreateSales> sales
        ) {
}
