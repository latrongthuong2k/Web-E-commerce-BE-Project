package com.ecommerce.myapp.Services.Implement;

import com.ecommerce.myapp.DTO.Bill.ReqCreateBill;
import com.ecommerce.myapp.DTO.Bill.ReqUpdateBill;
import com.ecommerce.myapp.DTO.Bill.ResBillDto;
import com.ecommerce.myapp.DTO.Bill.ResPageBill;
import com.ecommerce.myapp.Entity.Bill.Bill;
import com.ecommerce.myapp.Entity.Bill.BillStatus;
import com.ecommerce.myapp.Entity.Bill.Purchases;
import com.ecommerce.myapp.Entity.Bill.Sales;
import com.ecommerce.myapp.Exceptions.CannotDeleteException;
import com.ecommerce.myapp.Exceptions.EmailSendingException;
import com.ecommerce.myapp.Exceptions.ResourceNotFoundException;
import com.ecommerce.myapp.Repositories.BillRepository;
import com.ecommerce.myapp.Repositories.PurchasesRepository;
import com.ecommerce.myapp.Repositories.SalesRepository;
import com.ecommerce.myapp.Services.BillService;
import com.ecommerce.myapp.Services.EmailService;
import com.ecommerce.myapp.Users.Audit.AuditService;
import com.ecommerce.myapp.Users.Service.UserService;
import com.ecommerce.myapp.Users.security.Auth.AuthenticationService;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

import javax.security.sasl.AuthenticationException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class BillServiceImpl implements BillService {
    private final PurchasesRepository purchasesRepository;
    private final SalesRepository salesRepository;
    private final BillRepository billRepository;
    private final EmailService emailService;
    private final UserService userService;
    private final AuditService auditService;


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
//            List<Purchases> purchases = purchasesRepository.findByBillId(billId);
//            List<Sales> sales = salesRepository.findByBillId(billId);

            bill.setApprovedBy(auditService.getAuditorId().orElseThrow(
                    () -> new BadCredentialsException("Can't get auditor form token")));
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
