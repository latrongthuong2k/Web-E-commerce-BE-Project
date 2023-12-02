package com.ecommerce.myapp.Utils;

import com.ecommerce.myapp.DTO.ProductCartDetailDDTO;
import com.ecommerce.myapp.Entity.ShopingCart.ProductCartDetail;

import java.util.List;
import java.util.stream.Collectors;
public class ProductCartDetailMapper {
    public static ProductCartDetailDDTO toDTO(ProductCartDetail entity) {
        return new ProductCartDetailDDTO(entity.getCart().getId(),
                entity.getProduct().getId(),entity.getQuantity(),
                entity.getComment());
    }

    // Convert a list of entities to a list of DTOs
    public static List<ProductCartDetailDDTO> toDTOs(List<ProductCartDetail> entities) {
        return entities.stream()
                .map(ProductCartDetailMapper::toDTO)
                .collect(Collectors.toList());
    }
}
