package com.ecommerce.myapp.DTO;

import com.ecommerce.myapp.Entity.ShopingCart.ProductCartDetail;
import jakarta.validation.constraints.NotNull;

/**
 * DTO for {@link ProductCartDetail}
 */
public record ProductCartDetailDDTO(
        @NotNull Integer cart,
        @NotNull Integer product,
        @NotNull Integer quantity,
        String comment
){
}