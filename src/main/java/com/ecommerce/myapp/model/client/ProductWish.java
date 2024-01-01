package com.ecommerce.myapp.model.client;

import com.ecommerce.myapp.model.group.Product;
import com.ecommerce.myapp.model.user.AppUser;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "wish_list")
public class ProductWish {
    @Id
    @Column(name = "wish_list_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private AppUser appUser;

    @JsonIgnore
    @JoinColumn(name = "product_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Product product;

}
