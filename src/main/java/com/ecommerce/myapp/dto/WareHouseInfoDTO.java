package com.ecommerce.myapp.dto;

import com.ecommerce.myapp.model.warehouse.Inventory;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record WareHouseInfoDTO(@NotNull String wareHouseName, List<Inventory> inventories) {
}
