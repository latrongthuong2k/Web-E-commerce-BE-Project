package com.ecommerce.myapp.Repositories.Product;

import com.ecommerce.myapp.DTO.Product.ClientTypeDto;
import com.ecommerce.myapp.Entity.ProductConnectEntites.ClientType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ClientTypeRepository extends JpaRepository<ClientType, Integer> {
    @Query("SELECT new com.ecommerce.myapp.DTO.Product.ClientTypeDto(s.id, s.typeName) FROM ClientType s")
    List<ClientTypeDto> findAllClientTypes();
    @Query("SELECT new com.ecommerce.myapp.DTO.Product.ClientTypeDto(c.id, c.typeName) FROM ClientType c JOIN c.products p WHERE p.id = :productId")
    List<ClientTypeDto> findByProductId(Integer productId);
}