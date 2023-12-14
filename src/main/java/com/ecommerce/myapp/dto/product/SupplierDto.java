package com.ecommerce.myapp.dto.product;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

public record SupplierDto(@NotNull(message = "suppliers id must not null") Integer id,
                          @NotNull String supplierName) implements Serializable {
}