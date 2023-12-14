package com.ecommerce.myapp.repositories.product;

import com.ecommerce.myapp.dto.product.ClientTypeDto;
import com.ecommerce.myapp.model.product.ClientType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ClientTypeRepository extends JpaRepository<ClientType, Integer> {
    @Query("SELECT new com.ecommerce.myapp.dto.product.ClientTypeDto(s.id, s.typeName) FROM ClientType s")
    List<ClientTypeDto> findAllClientTypes();
    @Query("SELECT new com.ecommerce.myapp.dto.product.ClientTypeDto(c.id, c.typeName) FROM ClientType c JOIN c.products p WHERE p.id = :productId")
    List<ClientTypeDto> findByProductId(Integer productId);
}