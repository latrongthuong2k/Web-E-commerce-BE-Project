package com.ecommerce.myapp.Entity.Bill;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "bills")
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @CreatedDate
    @Column(name = "create_date", nullable = false)
    private LocalDateTime createDate;

    @Column(name = "approve_date")
    private LocalDateTime approveDate;

    @CreatedBy
    @Column(name = "create_by", nullable = false)
    private Integer createBy;

    @Column(name = "approve_by")
    private Integer approveBy;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "sale_price",nullable = false, precision = 15, scale = 2)
    private BigDecimal purchasePrice;

    // Status
    @Enumerated(EnumType.STRING)
    @Column(name = "bill_status")
    private BillStatus billStatus;

    //**
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bill_id")
    private Bill bill_Id;


}
