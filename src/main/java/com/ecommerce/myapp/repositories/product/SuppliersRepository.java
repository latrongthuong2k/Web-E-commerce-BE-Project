package com.ecommerce.myapp.repositories.product;

import com.ecommerce.myapp.dto.product.SupplierDto;
import com.ecommerce.myapp.model.bill.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SuppliersRepository extends JpaRepository<Supplier, Integer> {
    @Query("SELECT new com.ecommerce.myapp.dto.product.SupplierDto(s.id, s.supplierName) FROM Supplier s")
    List<SupplierDto> findAllSupplier();
}