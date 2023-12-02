package com.ecommerce.myapp.Entity.ProductConnectEntites;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table( name = "warehouse")
public class Warehouse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "warehouse_name", nullable = false)
    private String warehouseName;

    @OneToMany(mappedBy = "warehouseId")
    private List<Inventory> inventories = new ArrayList<>();
}
