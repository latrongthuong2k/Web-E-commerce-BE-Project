package com.ecommerce.myapp.repositories;

import com.ecommerce.myapp.model.warehouse.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WarehouseRepository extends JpaRepository<Warehouse, Integer> {
}