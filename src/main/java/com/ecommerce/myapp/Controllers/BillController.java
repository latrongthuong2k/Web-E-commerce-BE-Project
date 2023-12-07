package com.ecommerce.myapp.Controllers;

import com.ecommerce.myapp.DTO.Bill.ReqUpdateBill;
import com.ecommerce.myapp.DTO.Bill.ResBillDto;
import com.ecommerce.myapp.DTO.Bill.ResPageBill;
import com.ecommerce.myapp.Services.BillService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/orders")
public class BillController {
    private final BillService billService;
//    private final CacheManager cacheManager;

    //    @CacheEvict(value = "billPage", allEntries = true)
//    @PostMapping("create")
//    public ResponseEntity<String> createBill(@Valid @RequestBody ReqCreateBill reqCreateBill) {
//        billService.saveBill(reqCreateBill);
//        return ResponseEntity.ok("Bill created successfully");
//    }
    @CacheEvict(value = "billPage", allEntries = true)
    @PutMapping("/update")
    public ResponseEntity<String> updateBill(@RequestParam("billId") Integer billId, @Valid @RequestBody ReqUpdateBill reqCreateBill) {
        billService.updateBillById(billId, reqCreateBill);
        return ResponseEntity.ok().body("Bill status is updated successfully");
    }

    //    @GetMapping("/{billId}")
//    public ResponseEntity<String> getUserById(@PathVariable Integer billId) {
//        ReqCreateBill reqCreateBill = billService.findById(billId);
//        return ResponseEntity.ok().body("Bill found: " + reqCreateBill.categoryName());
//    }
    @GetMapping("/get")
    public ResponseEntity<ResBillDto> getBillById(@RequestParam(name = "billId") Integer id) {
        return ResponseEntity.ok(billService.findBillById(id));
    }


//    @Cacheable(value = "billPage", key = "{#page,#sortField,#sortDir,#query}")
    @GetMapping("/page")
    public ResponseEntity<Map<String, Object>> getBillPage(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size,
            @RequestParam(name = "q", defaultValue = "") String query,
            @RequestParam(name = "sortField", defaultValue = "createdDate") String sortField,
            @RequestParam(name = "sortDir", defaultValue = "asc") String sortDir) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDir.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortField));
        Page<ResPageBill> bills = billService.getBillPage(query, pageable);
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
        billService.cancelBill(billId);
        return ResponseEntity.ok().body("Bill canceled successfully");
    }
}
