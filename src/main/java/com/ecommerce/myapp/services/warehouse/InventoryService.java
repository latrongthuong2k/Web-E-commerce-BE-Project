package com.ecommerce.myapp.services.warehouse;

import com.ecommerce.myapp.model.warehouse.Inventory;

public interface InventoryService {

    Inventory getInventoryByProductId(Integer productId);
}
