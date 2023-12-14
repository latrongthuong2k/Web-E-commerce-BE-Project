package com.ecommerce.myapp.dto.Mapper;

import com.ecommerce.myapp.dto.product.ProductBasicInfoDTO;
import com.ecommerce.myapp.model.product.Product;
import jakarta.validation.Valid;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductPageMapper {

    Product toEntity(@Valid ProductBasicInfoDTO productBasicInfoDTO);
    ProductBasicInfoDTO toDto(Product product);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Product partialUpdate(ProductBasicInfoDTO productBasicInfoDTO, @MappingTarget Product product);
}