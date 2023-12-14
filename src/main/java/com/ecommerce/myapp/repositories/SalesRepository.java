package com.ecommerce.myapp.repositories;

import com.ecommerce.myapp.model.bill.Sales;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SalesRepository extends JpaRepository<Sales, Integer> {
    @Query("select p from Sales p where p.bill.id = ?1")
    List<Sales> findByBillId (Integer billId);
}