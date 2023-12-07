package com.ecommerce.myapp.Repositories;

import com.ecommerce.myapp.Entity.Bill.Purchases;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PurchasesRepository extends JpaRepository<Purchases, Integer> {
    @Query("select p from Purchases p where p.bill.id = ?1")
    List<Purchases> findByBillId (Integer billId);
}