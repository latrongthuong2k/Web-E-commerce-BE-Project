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
@Table(name = "client_types")
public class ClientType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "type_Name", nullable = false)
    private String typeName;

    @ManyToMany(mappedBy = "clientType")
    private List<Product> products = new ArrayList<>();

}
