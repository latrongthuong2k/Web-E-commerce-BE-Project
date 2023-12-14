package com.ecommerce.myapp.dto.Mapper;

import com.ecommerce.myapp.dto.category.ReqCreateCategory;
import com.ecommerce.myapp.dto.category.ResCategory;
import com.ecommerce.myapp.model.product.Category;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface CategoryMapper {
    Category toEntity(ReqCreateCategory reqCreateCategory);

    ResCategory toDto(Category category);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Category partialUpdate(ReqCreateCategory reqCreateCategory, @MappingTarget Category category);
}