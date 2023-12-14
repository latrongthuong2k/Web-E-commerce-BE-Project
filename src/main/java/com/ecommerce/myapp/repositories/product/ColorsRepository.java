package com.ecommerce.myapp.repositories.product;

import com.ecommerce.myapp.dto.product.ColorsDto;
import com.ecommerce.myapp.model.product.Colors;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ColorsRepository extends JpaRepository<Colors, Integer> {
    @Query("SELECT new com.ecommerce.myapp.dto.product.ColorsDto(c.id, c.colorName) FROM Colors c")
    List<ColorsDto> findAllColors();

    @Query("SELECT new com.ecommerce.myapp.dto.product.ColorsDto(c.id, c.colorName) FROM Colors c JOIN c.products p WHERE p.id = :productId")
    List<ColorsDto> findByProductId(Integer productId);
}