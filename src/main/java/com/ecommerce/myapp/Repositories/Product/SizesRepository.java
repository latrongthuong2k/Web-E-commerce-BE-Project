package com.ecommerce.myapp.Repositories.Product;


import com.ecommerce.myapp.DTO.Product.SizesDto;
import com.ecommerce.myapp.Entity.ProductConnectEntites.Sizes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SizesRepository extends JpaRepository<Sizes, Integer> {
    @Query("SELECT new com.ecommerce.myapp.DTO.Product.SizesDto(s.id, s.sizeValue) FROM Sizes s")
    List<SizesDto> findAllSizes();
    @Query("SELECT new com.ecommerce.myapp.DTO.Product.SizesDto(c.id, c.sizeValue) FROM Sizes c JOIN c.products p WHERE p.id = :productId")
    List<SizesDto> findByProductId(Integer productId);
}