package com.ecommerce.myapp.repositories;

import com.ecommerce.myapp.model.group.ProductSize;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductSizeRepository extends JpaRepository<ProductSize, Long> {
}