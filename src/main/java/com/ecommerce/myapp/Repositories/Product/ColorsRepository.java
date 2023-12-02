package com.ecommerce.myapp.Repositories.Product;

import com.ecommerce.myapp.DTO.Product.ColorsDto;
import com.ecommerce.myapp.Entity.ProductConnectEntites.Colors;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ColorsRepository extends JpaRepository<Colors, Integer> {
    @Query("SELECT new com.ecommerce.myapp.DTO.Product.ColorsDto(c.id, c.colorName) FROM Colors c")
    List<ColorsDto> findAllColors();

    @Query("SELECT new com.ecommerce.myapp.DTO.Product.ColorsDto(c.id, c.colorName) FROM Colors c JOIN c.products p WHERE p.id = :productId")
    List<ColorsDto> findByProductId(Integer productId);
}