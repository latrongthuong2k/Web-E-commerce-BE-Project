package com.ecommerce.myapp.Repositories;

import com.ecommerce.myapp.Entity.Bill.Bill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.Optional;

public interface BillRepository extends JpaRepository<Bill, Integer> {
    @Query("select b from Bill b where cast(b.id as string ) = ?1 or cast(b.approvedDate as string) like concat('%',?1,'%')" +
           "or cast(b.billStatus as string ) like concat('%',?1,'%') ")
    Page<Bill> searchBill(String search, Pageable pageable);

    @Query("SELECT COUNT(b) FROM Bill b WHERE DATE(b.createdDate) = CURRENT_DATE")
    Long countBillsToday();

    @Query("SELECT COUNT(b) FROM Bill b WHERE b.createdDate >= ?1 AND b.createdDate <= ?2")
    Long countBillsThisWeek(LocalDate startOfWeek, LocalDate endOfWeek);

    @Query("SELECT COUNT(b) FROM Bill b WHERE MONTH(b.createdDate) = MONTH(CURRENT_DATE) AND YEAR(b.createdDate) = YEAR(CURRENT_DATE)")
    Long countBillsThisMonth();


}