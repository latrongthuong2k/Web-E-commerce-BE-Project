package com.ecommerce.myapp.Entity;

import com.ecommerce.myapp.Entity.ProductConnectEntites.Product;
import com.ecommerce.myapp.Users.Entity.AppUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false,foreignKey = @ForeignKey(name = "product_id_reviews_fk"))
    private Product product;

    @Column(name = "column_name", nullable = false)
    private Integer columnName;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false,foreignKey = @ForeignKey(name = "user_id_reviews_fk"))
    private AppUser appUser;

    @Column(name = "rating", nullable = false)
    private Integer rating;

    @Lob
    @Column(name = "comment")
    private String comment;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

}