package com.ecommerce.myapp.model.client;

import com.ecommerce.myapp.model.product.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product_cart_details")
public class ProductCartDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    // Bất kì cart nào
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cart_id", nullable = false, foreignKey = @ForeignKey(name = "cart_id_detail_fk"))
    private Cart cart;

    // Bất kì product nào
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false, foreignKey = @ForeignKey(name = "product_id_cart_fk"))
    private Product product;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Lob
    @Column(name = "comment")
    private String comment;

}

