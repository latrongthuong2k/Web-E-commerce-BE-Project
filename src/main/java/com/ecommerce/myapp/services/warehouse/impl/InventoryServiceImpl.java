package com.ecommerce.myapp.services.warehouse.impl;

import com.ecommerce.myapp.model.warehouse.Inventory;
import com.ecommerce.myapp.repositories.product.InventoryRepository;
import com.ecommerce.myapp.services.warehouse.InventoryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional(rollbackOn = Exception.class)
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;

    @Override
    public Inventory getInventoryByProductId(Integer productId) {
        return inventoryRepository.findByProductId(productId);
    }
    // Services


}
