package com.ecommerce.myapp.model.client;

import com.ecommerce.myapp.model.user.AppUser;
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
@Table(name = "carts")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    // bất kì appUser nào - với Cart
    @OneToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "User_id",nullable = false,foreignKey = @ForeignKey(name = "user_id_cart_fk"))

    private AppUser appUser;

    /**
     *  Liên kết 1-n từ 1 cart đến bảng phụ ProductCartDetail
     *  1 cart có thể có nhiều product
     */
    @OneToMany(mappedBy = "cart",cascade = CascadeType.ALL)
    private List<ProductCartDetail> productCartDetails = new ArrayList<>();

}