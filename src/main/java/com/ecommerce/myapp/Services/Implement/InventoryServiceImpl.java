package com.ecommerce.myapp.Services.Implement;

import com.ecommerce.myapp.Entity.ProductConnectEntites.Inventory;
import com.ecommerce.myapp.Repositories.Product.InventoryRepository;
import com.ecommerce.myapp.Services.InventoryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;

    @Override
    public Inventory getInventoryByProductId(Integer productId) {
        return inventoryRepository.findByProductId(productId);
    }
    // Services


}
