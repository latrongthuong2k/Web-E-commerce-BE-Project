package com.ecommerce.myapp.Services;


import com.ecommerce.myapp.DTO.Bill.ReqUpdateBill;
import com.ecommerce.myapp.DTO.Bill.ResBillDto;
import com.ecommerce.myapp.DTO.Bill.ResPageBill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface BillService {

    Long statisticalBillByDay();
    Long statisticalBillByWeek();
    Long statisticalBillByMonth();

    void updateBillById(Integer billId, ReqUpdateBill reqCreateBill);

    Page<ResPageBill> getBillPage(String query, Pageable pageable);

    ResBillDto findBillById(Integer id);

    void cancelBill(Integer billId);
}
