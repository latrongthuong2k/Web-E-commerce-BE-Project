package com.ecommerce.myapp.model.checkoutGroup;

import com.ecommerce.myapp.model.group.Product;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "order_details")
public class OrderDetail {
    @Id
    @Column(name = "id")
    private Long id;

    @JoinColumn(name = "order_id")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Order order;

    @JoinColumn(name = "product_id")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Product product;

    @Column(name = "product_name", nullable = false, length = 100)
    private String productName;

    @Column(name = "unit_price", nullable = false, length = 100, precision = 10, scale = 2)
    private BigDecimal unitPrice;

    @Min(0)
    @Column(name = "order_quantity", nullable = false, length = 100)
    private Integer orderQuantity;


}
