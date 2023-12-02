package com.ecommerce.myapp.DTO;

import com.ecommerce.myapp.Entity.ProductConnectEntites.Inventory;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record WareHouseInfoDTO(@NotNull String wareHouseName, List<Inventory> inventories) {
}
