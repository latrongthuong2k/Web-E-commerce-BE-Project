package com.ecommerce.myapp.services.client.impl;

import com.ecommerce.myapp.dto.bill.ReqCreateBill;
import com.ecommerce.myapp.dto.bill.ReqCreateSales;
import com.ecommerce.myapp.exceptions.ResourceNotFoundException;
import com.ecommerce.myapp.model.bill.Bill;
import com.ecommerce.myapp.model.bill.Sales;
import com.ecommerce.myapp.repositories.BillRepository;
import com.ecommerce.myapp.services.admin.ProductService;
import com.ecommerce.myapp.services.client.ClientBillService;
import com.ecommerce.myapp.services.warehouse.InventoryService;
import com.ecommerce.myapp.services.warehouse.WarehouseService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional(rollbackOn = Exception.class)
@RequiredArgsConstructor
public class ClientBillServiceImpl implements ClientBillService {

    private final BillRepository billRepository;
    private final WarehouseService warehouseService;
    private final InventoryService inventoryService;
    private final ProductService productService;


    public void createBill(ReqCreateBill reqCreateBill) {
        Bill newBill = new Bill();
//        Purchases sales = new Purchases();
        List<ReqCreateSales> saleReq = reqCreateBill.sales();
        List<Sales> saleList = saleReq.stream().map(
                req -> Sales.builder()
                        .quantity(req.quantity())
                        .purchasePrice(productService.getProductById(req.productId()).getPrice())
                        .bill(newBill)
                        .product(productService.getProductById(req.productId()))
                        .build()).toList();
        newBill.setSales(saleList);
        billRepository.save(newBill);
    }

    private Bill foundBillById(Integer id) {
        return billRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Bill with id" + id + "can't find"));
    }
}
