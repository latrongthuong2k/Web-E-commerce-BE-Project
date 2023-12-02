package com.ecommerce.myapp.Entity.ProductConnectEntites;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "inventory")
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @OneToMany (mappedBy = "inventory")
    private List<Product> products;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "warehouse_id",nullable = false)
    private Warehouse warehouseId;

    // số hàng trong kho
    @Column(name = "stock_quantity", nullable = false)
    private Integer stockQuantity;

    @LastModifiedDate
    @Column(name = "last_update_at", insertable = false)
    private LocalDateTime lastUpdate;
}
