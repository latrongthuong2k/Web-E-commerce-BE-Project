package com.ecommerce.myapp.repositories;

import com.ecommerce.myapp.model.product.Category;
import com.ecommerce.myapp.model.product.CategoryImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CategoryImageRepository extends JpaRepository<CategoryImage, Integer> {
    Optional<CategoryImage> findByCategory(Category category);
    @Query("select ci from CategoryImage ci where ci.category.id = ?1")
    Optional<CategoryImage> findByCategoryId(Integer categoryId);
}