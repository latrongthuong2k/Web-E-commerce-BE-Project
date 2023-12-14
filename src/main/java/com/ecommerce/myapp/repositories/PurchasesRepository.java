package com.ecommerce.myapp.repositories;

import com.ecommerce.myapp.model.bill.Purchases;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PurchasesRepository extends JpaRepository<Purchases, Integer> {
    @Query("select p from Purchases p where p.bill.id = ?1")
    List<Purchases> findByBillId (Integer billId);
}