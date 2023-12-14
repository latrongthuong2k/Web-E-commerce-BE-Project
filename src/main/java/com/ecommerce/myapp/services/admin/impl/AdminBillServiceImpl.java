package com.ecommerce.myapp.services.admin.impl;

import com.ecommerce.myapp.dto.bill.ReqUpdateBill;
import com.ecommerce.myapp.dto.bill.ResBillDto;
import com.ecommerce.myapp.dto.bill.ResPageBill;
import com.ecommerce.myapp.exceptions.CannotDeleteException;
import com.ecommerce.myapp.exceptions.EmailSendingException;
import com.ecommerce.myapp.exceptions.ResourceNotFoundException;
import com.ecommerce.myapp.model.bill.Bill;
import com.ecommerce.myapp.model.bill.BillStatus;
import com.ecommerce.myapp.repositories.BillRepository;
import com.ecommerce.myapp.repositories.PurchasesRepository;
import com.ecommerce.myapp.repositories.SalesRepository;
import com.ecommerce.myapp.services.admin.AdminBillService;
import com.ecommerce.myapp.services.app.AppAuditAwareService;
import com.ecommerce.myapp.services.app.EmailService;
import com.ecommerce.myapp.services.user.UserService;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;

@Service
@Transactional(rollbackOn = Exception.class)
@AllArgsConstructor
public class AdminBillServiceImpl implements AdminBillService {
    private final PurchasesRepository purchasesRepository;
    private final SalesRepository salesRepository;
    private final BillRepository billRepository;
    private final EmailService emailService;
    private final UserService userService;
    private final AppAuditAwareService auditAwareService;


    @Override
    public Long statisticalBillByDay() {
        return billRepository.countBillsToday();
    }

    @Override
    public Long statisticalBillByWeek() {
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(DayOfWeek.MONDAY);
        LocalDate endOfWeek = today.with(DayOfWeek.SUNDAY);
        return billRepository.countBillsThisWeek(startOfWeek,endOfWeek);
    }

    @Override
    public Long statisticalBillByMonth() {
        return billRepository.countBillsThisMonth();
    }

    @Override
    public void updateBillById(Integer billId, ReqUpdateBill reqCreateBill) {
        Bill bill = foundBillById(billId);
        bill.setBillStatus(reqCreateBill.status());
        String subject = "";
        String content = "";
        if (bill.getBillStatus().equals(BillStatus.APPROVE)) {
//            List<Purchases> sales = purchasesRepository.findByBillId(billId);
//            List<Sales> sales = salesRepository.findByBillId(billId);

            bill.setApprovedBy(auditAwareService.getCurrentAuditor().orElseThrow(
                    () -> new BadCredentialsException("Can't get auditor form token")));
//            bill.setApprovedBy(16);
            subject = "Your order has been confirmed ";
            content = "Your order will be delivered as soon as possible," +
                      " we will calculate and notify you of the estimated delivery time, thank you.";
        }
//        } else if (bill.getBillStatus().equals(BillStatus.CANCEL)) {
//            subject = "Your order has been canceled ";
//            content = "Your order has been canceled, the reason is ..... so we cannot send it," +
//                      " we sincerely apologize to you";
//        }
        if (bill.getBillStatus().equals(BillStatus.APPROVE)) {
            try {
                emailService.sendEmail(reqCreateBill.customerEmail(), subject, content);
                billRepository.save(bill);
            } catch (MessagingException e) {
                throw new EmailSendingException("Error send email", e);
            }
        }
    }

    @Override
    public Page<ResPageBill> getBillPage(String query, Pageable pageable) {
        Page<Bill> bills = billRepository.searchBill(query, pageable);
        return bills.map(bill -> new ResPageBill(
                bill.getId(),
                bill.getCreatedDate(),
                bill.getApprovedDate(),
                userService.findById(bill.getCreatedBy()).getFirstName(), // createBy
                userService.findById(bill.getApprovedBy()) != null ? userService.findById(bill.getApprovedBy()).getFirstName() : null,
                bill.getBillStatus()));
    }

    @Override
    public ResBillDto findBillById(Integer id) {
        Bill bill = foundBillById(id);
        return new ResBillDto(bill.getId(), bill.getAppUser().getEmail(), bill.getBillStatus());
    }

    @Override
    public void cancelBill(Integer billId) {
        Bill bill = foundBillById(billId);
        if (bill.getBillStatus().equals(BillStatus.APPROVE)) {
            throw new CannotDeleteException("The bill cannot be deleted because the bill has been approved");
        }
        bill.setBillStatus(BillStatus.CANCEL);
    }

    private Bill foundBillById(Integer id) {
        return billRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Bill with id" + id + "can't find"));
    }
}
