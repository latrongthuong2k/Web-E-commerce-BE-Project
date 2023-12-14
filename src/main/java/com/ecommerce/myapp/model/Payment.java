package com.ecommerce.myapp.model;

import com.ecommerce.myapp.model.bill.Bill;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "payment")
public class Payment {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "bill_id", nullable = false)
    private Bill bill;

    @Size(max = 20)
    @Column(name = "payment_type", nullable = false, length = 20)
    private String paymentType;

    @Size(max = 20)
    @Column(name = "status", nullable = false, length = 20)
    private String status;

}