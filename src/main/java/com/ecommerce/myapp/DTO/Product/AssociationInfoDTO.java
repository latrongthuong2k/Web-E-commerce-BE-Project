package com.ecommerce.myapp.DTO.Product;

import com.ecommerce.myapp.Entity.Bill.Supplier;
import com.ecommerce.myapp.Entity.ProductConnectEntites.*;
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
