package com.ecommerce.myapp.repositories;

import com.ecommerce.myapp.model.group.Category;
import com.ecommerce.myapp.model.group.CategoryImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryImageRepository extends JpaRepository<CategoryImage, Integer> {
    List<CategoryImage> findAllByCategory(Category category);

    Optional<CategoryImage> findByCategory(Category category);
}