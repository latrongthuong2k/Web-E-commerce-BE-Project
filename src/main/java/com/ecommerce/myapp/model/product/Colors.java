package com.ecommerce.myapp.model.product;

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
@Table( name = "colors")
public class Colors {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "color_name" ,nullable = false)
    private String colorName;

    @Column(name = "color_Image_Url" ,nullable = false)
    private String colorImageUrl;

    @ManyToMany(mappedBy = "colors")
    private List<Product> products = new ArrayList<>();


}
