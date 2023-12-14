package com.ecommerce.myapp.services.admin;


import com.ecommerce.myapp.dto.bill.ReqUpdateBill;
import com.ecommerce.myapp.dto.bill.ResBillDto;
import com.ecommerce.myapp.dto.bill.ResPageBill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface AdminBillService {

    Long statisticalBillByDay();
    Long statisticalBillByWeek();
    Long statisticalBillByMonth();

    void updateBillById(Integer billId, ReqUpdateBill reqCreateBill);

    Page<ResPageBill> getBillPage(String query, Pageable pageable);

    ResBillDto findBillById(Integer id);

    void cancelBill(Integer billId);
}
