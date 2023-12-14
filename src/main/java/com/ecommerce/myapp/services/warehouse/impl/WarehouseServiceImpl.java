package com.ecommerce.myapp.services.warehouse.impl;

import com.ecommerce.myapp.exceptions.ResourceNotFoundException;
import com.ecommerce.myapp.model.warehouse.Warehouse;
import com.ecommerce.myapp.repositories.WarehouseRepository;
import com.ecommerce.myapp.services.warehouse.WarehouseService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional(rollbackOn = Exception.class)
@RequiredArgsConstructor
public class WarehouseServiceImpl implements WarehouseService {

//    private final InventoryService inventoryService;
    private final WarehouseRepository warehouseRepository;

    @Override
    public Warehouse getWarehouseById(Integer id){
       return  warehouseRepository.findById(id).orElseThrow(
               ()-> new ResourceNotFoundException("Warehouse id" + id + "is not exist"));
    }
}
