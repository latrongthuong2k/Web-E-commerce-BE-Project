package com.ecommerce.myapp.DTO.Mapper;

import com.ecommerce.myapp.DTO.Category.ReqCreateCategory;
import com.ecommerce.myapp.Entity.ProductConnectEntites.Category;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface CategoryMapper {
    Category toEntity(ReqCreateCategory reqCreateCategory);

    ReqCreateCategory toDto(Category category);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Category partialUpdate(ReqCreateCategory reqCreateCategory, @MappingTarget Category category);
}