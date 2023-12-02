package com.ecommerce.myapp.DTO.Category;


import com.ecommerce.myapp.Entity.ProductConnectEntites.Category;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

/**
 * DTO for {@link Category}
 */
public record ParentCategoryDto(
        @NotNull Integer id,
        @NotNull @Size(max = 30) String categoryName
) implements Serializable {
}