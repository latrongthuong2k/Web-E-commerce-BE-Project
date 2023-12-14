package com.ecommerce.myapp.repositories.product;

import com.ecommerce.myapp.model.product.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Page<Category> findByCategoryNameContainingIgnoreCase(String categoryName, Pageable pageable);
    Optional<Category> findByCategoryName(String categoryName);
}