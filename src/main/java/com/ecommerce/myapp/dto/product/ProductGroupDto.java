package com.ecommerce.myapp.dto.product;

import com.ecommerce.myapp.dto.category.ResCategory;

import java.util.List;

public record ProductGroupDto(
        List<ResCategory> categories,
        List<ColorsDto> colors,
        List<SizesDto> sizes,
        List<TagsDto> tags,
        List<ClientTypeDto> clientTypes,
        List<SupplierDto> suppliers) {
}
