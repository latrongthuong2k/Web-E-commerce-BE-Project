package com.ecommerce.myapp.Repositories;

import com.ecommerce.myapp.Entity.Bill.Bill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillRepository extends JpaRepository<Bill, Integer> {
}