package com.ecommerce.myapp.Repositories.Product;

import com.ecommerce.myapp.Entity.ProductConnectEntites.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    @Query("FROM Category c WHERE LOWER(c.categoryName) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Category> findByCategoryNameContainingIgnoreCase(@Param("search") String search, Pageable pageable);
}