package com.ecommerce.myapp.dto.product;

import com.ecommerce.myapp.model.bill.Supplier;
import com.ecommerce.myapp.model.product.*;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record AssociationInfoDTO(
        @NotNull List<Category> category,
        @NotNull List<Supplier> supplier,
        @NotNull List<ClientType> clientType,
        @NotNull List<Colors> color,
        @NotNull List<Sizes> size,
        @NotNull List<Tags> tag) {
}
