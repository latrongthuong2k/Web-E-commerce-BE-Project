package com.ecommerce.myapp.model.client;

import com.ecommerce.myapp.dtos.product.response.ResSimpleInfoProductDto;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

/**
 * DTO for {@link ProductWish}
 */
public record WishListDto(Long id, @NotNull ResSimpleInfoProductDto product) implements Serializable {
}