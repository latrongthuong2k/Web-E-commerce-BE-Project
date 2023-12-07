package com.ecommerce.myapp.Repositories;

import com.ecommerce.myapp.Entity.Bill.Purchases;
import com.ecommerce.myapp.Entity.Bill.Sales;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SalesRepository extends JpaRepository<Sales, Integer> {
    @Query("select p from Sales p where p.bill_Id.id = ?1")
    List<Sales> findByBillId (Integer billId);
}