package com.ecommerce.myapp.DTO.Mapper;

import com.ecommerce.myapp.DTO.Product.ProductBasicInfoDTO;
import com.ecommerce.myapp.Entity.ProductConnectEntites.Product;
import jakarta.validation.Valid;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductMapper {

    Product toEntity(@Valid ProductBasicInfoDTO productBasicInfoDTO);
    ProductBasicInfoDTO toDto(Product product);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Product partialUpdate(ProductBasicInfoDTO productBasicInfoDTO, @MappingTarget Product product);
}