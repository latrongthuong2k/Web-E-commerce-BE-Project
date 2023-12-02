package com.ecommerce.myapp.Entity;

import com.ecommerce.myapp.Entity.ProductConnectEntites.Product;
import com.ecommerce.myapp.Users.Entity.AppUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "wish_list")
public class WishList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser appUser;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @ManyToMany(mappedBy = "wishLists")
    private List<Product> products = new ArrayList<>();
}