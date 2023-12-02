package com.ecommerce.myapp.Repositories.Product;

import com.ecommerce.myapp.DTO.Product.SupplierDto;
import com.ecommerce.myapp.Entity.Bill.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SuppliersRepository extends JpaRepository<Supplier, Integer> {
    @Query("SELECT new com.ecommerce.myapp.DTO.Product.SupplierDto(s.id, s.supplierName) FROM Supplier s")
    List<SupplierDto> findAllSupplier();
}