package com.ecommerce.myapp.DTO.Product;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

public record SupplierDto(@NotNull(message = "suppliers id must not null") Integer id,
                          @NotNull String supplierName) implements Serializable {
}