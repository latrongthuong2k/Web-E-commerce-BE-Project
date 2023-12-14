package com.ecommerce.myapp.controllers.admin;

import com.ecommerce.myapp.dto.bill.ReqUpdateBill;
import com.ecommerce.myapp.dto.bill.ResBillDto;
import com.ecommerce.myapp.dto.bill.ResPageBill;
import com.ecommerce.myapp.services.admin.AdminBillService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/orders")
@PreAuthorize("hasRole('ADMIN')")
public class BillController {
    private final AdminBillService adminBillService;

    //    @CacheEvict(value = "billPage", allEntries = true)
//    @PostMapping("create")
//    public ResponseEntity<String> createBill(@Valid @RequestBody ReqCreateBill reqCreateBill) {
//        billService.saveBill(reqCreateBill);
//        return ResponseEntity.ok("Bill created successfully");
//    }
    @CacheEvict(value = "billPage", allEntries = true)
    @PutMapping("/update")
    public ResponseEntity<String> updateBill(@RequestParam("billId") Integer billId, @Valid @RequestBody ReqUpdateBill reqCreateBill) {
        adminBillService.updateBillById(billId, reqCreateBill);
        return ResponseEntity.ok().body("Bill status is updated successfully");
    }

    //    @GetMapping("/{billId}")
//    public ResponseEntity<String> getUserById(@PathVariable Integer billId) {
//        ReqCreateBill reqCreateBill = billService.findById(billId);
//        return ResponseEntity.ok().body("Bill found: " + reqCreateBill.categoryName());
//    }
    @GetMapping("/get")
    public ResponseEntity<ResBillDto> getBillById(@RequestParam(name = "billId") Integer id) {
        return ResponseEntity.ok(adminBillService.findBillById(id));
    }


    @Cacheable(value = "billPage", key = "{#page,#sortField,#sortDir,#query}")
    @GetMapping("/page")
    public ResponseEntity<Map<String, Object>> getBillPage(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size,
            @RequestParam(name = "q", defaultValue = "") String query,
            @RequestParam(name = "sortField", defaultValue = "createdDate") String sortField,
            @RequestParam(name = "sortDir", defaultValue = "asc") String sortDir) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDir.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortField));
        Page<ResPageBill> bills = adminBillService.getBillPage(query, pageable);
        Map<String, Object> response = new HashMap<>();
//        response.put("currentPage", productPage.getNumber());
//        response.put("totalItems", productPage.getTotalElements());
        response.put("bills", bills.getContent());
        response.put("totalPages", bills.getTotalPages());
        return ResponseEntity.ok(response);
    }

    @CacheEvict(value = "billPage", allEntries = true)
    @DeleteMapping("/cancel")
    public ResponseEntity<String> deleteBill(@RequestParam("billId") Integer billId) {
        adminBillService.cancelBill(billId);
        return ResponseEntity.ok().body("Bill canceled successfully");
    }
}
