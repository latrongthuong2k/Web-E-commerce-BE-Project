package com.ecommerce.myapp.Services;

import com.ecommerce.myapp.Entity.ProductConnectEntites.Inventory;

public interface InventoryService {

    Inventory getInventoryByProductId(Integer productId);
}
