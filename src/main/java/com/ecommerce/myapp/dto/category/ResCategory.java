package com.ecommerce.myapp.dto.category;

import com.ecommerce.myapp.model.product.Category;

import java.io.Serializable;

/**
 * DTO for {@link Category}
 */
public record ResCategory(
        Integer id,
        String categoryName,
        String imageId,
        ParentCategoryDto parentCategory
) implements Serializable {
}