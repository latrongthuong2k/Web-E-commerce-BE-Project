package com.ecommerce.myapp.dto.category;

import com.ecommerce.myapp.model.product.Category;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

/**
 * DTO for {@link Category}
 */
public record ReqUpdateCategory(
        @NotNull @Size(max = 30) String categoryName,
        Category parentCategory
) implements Serializable {
}