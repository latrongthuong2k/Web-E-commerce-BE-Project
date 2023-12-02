package com.ecommerce.myapp.Entity.Bill;

import com.ecommerce.myapp.Entity.ProductConnectEntites.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sales")
public class Sales {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id")
    private Product productId;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "sale_price",precision = 15, scale = 2, nullable = false)
    private BigDecimal purchasePrice;

    //**
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "bill_id")
    private Bill bill_Id;


}
