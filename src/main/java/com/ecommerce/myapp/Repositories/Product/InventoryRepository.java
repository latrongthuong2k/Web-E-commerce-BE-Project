package com.ecommerce.myapp.Repositories.Product;

import com.ecommerce.myapp.Entity.ProductConnectEntites.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface InventoryRepository extends JpaRepository<Inventory, Integer> {
    @Query("select i from Inventory i join i.products p where p.id = :productId")
    Inventory findByProductId(Integer productId);
}