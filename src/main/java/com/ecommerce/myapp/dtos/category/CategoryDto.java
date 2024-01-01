package com.ecommerce.myapp.dtos.category;

import com.ecommerce.myapp.model.group.Category;
import com.ecommerce.myapp.model.group.CategoryImage;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.Set;

/**
 * DTO for {@link Category}
 */
public record CategoryDto(
        @NotNull(message = "Category ID cannot be null")
        @PositiveOrZero(message = "Category ID must be a positive number or zero")
        Long id,
        @Size(max = 255, message = "Category name must not exceed 255 characters")
        @NotBlank(message = "Category name cannot be blank")
        String categoryName,
        String description,
        @NotNull(message = "Status cannot be null")
        String status,
        @NotNull
        Set<CategoryImage> categoryImage,
        MultipartFile imageFile
) implements Serializable {
}
