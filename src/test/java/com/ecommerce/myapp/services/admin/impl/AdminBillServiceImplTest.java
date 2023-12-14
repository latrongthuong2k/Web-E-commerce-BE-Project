package com.ecommerce.myapp.services.admin.impl;

import com.ecommerce.myapp.exceptions.ResourceNotFoundException;
import com.ecommerce.myapp.repositories.BillRepository;
import com.ecommerce.myapp.services.app.AppAuditAwareService;
import com.ecommerce.myapp.services.app.EmailService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.ecommerce.myapp.model.bill.BillStatus;
import com.ecommerce.myapp.dto.bill.ReqUpdateBill;
import com.ecommerce.myapp.model.bill.Bill;
import com.ecommerce.myapp.exceptions.EmailSendingException;
import jakarta.mail.MessagingException;

class AdminBillServiceImplTest {

    @Mock
    private BillRepository billRepository;

    @Mock
    private EmailService emailService;

    @Mock
    private AppAuditAwareService auditAwareService;

    @Mock
    private ReqUpdateBill reqUpdateBill;

    @Mock
    private Bill bill;

    @Test
    void testUpdateBillById_whenBillDoesNotExist_expectResourceNotFoundException() {
        MockitoAnnotations.openMocks(this);
        AdminBillServiceImpl billService = new AdminBillServiceImpl(null, null, billRepository, emailService, null, auditAwareService);
        when(billRepository.findById(anyInt())).thenReturn(java.util.Optional.ofNullable(null));
        assertThrows(ResourceNotFoundException.class, () -> {
            billService.updateBillById(1, reqUpdateBill);
        });
    }

    @Test
    void testUpdateBillById_whenMessageCannotBeSent_expectEmailSendingException() throws MessagingException {
        MockitoAnnotations.openMocks(this);
        AdminBillServiceImpl billService = new AdminBillServiceImpl(null, null, billRepository, emailService, null, auditAwareService);
        when(billRepository.findById(anyInt())).thenReturn(java.util.Optional.of(bill));
        when(bill.getBillStatus()).thenReturn(BillStatus.APPROVE);
        when(reqUpdateBill.status()).thenReturn(BillStatus.APPROVE);
        when(reqUpdateBill.customerEmail()).thenReturn("abc@gmail.com");
        doThrow(MessagingException.class).when(emailService).sendEmail(anyString(),
                anyString(), anyString());
        assertThrows(EmailSendingException.class, () -> {
            billService.updateBillById(1, reqUpdateBill);
        });
    }

    @Test
    void testUpdateBillById_whenBillExistAndMessageCanBeSent_ExpectNoException() throws MessagingException {
        MockitoAnnotations.openMocks(this);
        AdminBillServiceImpl billService = new AdminBillServiceImpl(null, null, billRepository, emailService, null, auditAwareService);
        when(billRepository.findById(anyInt())).thenReturn(java.util.Optional.of(bill));
        when(bill.getBillStatus()).thenReturn(BillStatus.APPROVE);
        when(reqUpdateBill.status()).thenReturn(BillStatus.APPROVE);
        when(reqUpdateBill.customerEmail()).thenReturn("abc@gmail.com");
        doNothing().when(emailService).sendEmail(anyString(),
                anyString(), anyString());
        assertDoesNotThrow(() -> {
            billService.updateBillById(1, reqUpdateBill);
        });
    }
}  