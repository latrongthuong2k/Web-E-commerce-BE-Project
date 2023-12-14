package com.ecommerce.myapp.model.bill;

import com.ecommerce.myapp.model.product.Product;
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
@Table(name = "supplier")
public class Supplier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Column(name = "supplier_name",nullable = false)
    private String supplierName;
    @Column(name = "contact_info",nullable = false)
    private String contactInfo;
    @OneToMany(mappedBy = "supplier")
    private List<Product> products = new ArrayList<>();
}
