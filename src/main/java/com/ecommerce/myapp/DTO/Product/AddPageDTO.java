package com.ecommerce.myapp.DTO.Product;

import com.ecommerce.myapp.DTO.Category.ReqCreateCategory;

import java.util.List;

public record AddPageDTO(
        List<ReqCreateCategory> categories,
        List<ColorsDto> colors,
        List<SizesDto> sizes,
        List<TagsDto> tags,
        List<ClientTypeDto> clientTypes,
        List<SupplierDto> suppliers) {
}
