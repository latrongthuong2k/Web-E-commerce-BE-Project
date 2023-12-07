package com.ecommerce.myapp.Entity;

import com.ecommerce.myapp.Users.Entity.AppUser;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "shipping_Address")
public class ShippingAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    @Size(max = 100)
    @Column(name = "address", nullable = false, length = 100)
    private String address;

    @Size(max = 30)
    @Column(name = "city", nullable = false, length = 30)
    private String city;

    @Size(max = 30)
    @Column(name = "state", nullable = false, length = 30)
    private String state;

    @Size(max = 25)
    @Column(name = "country", nullable = false, length = 25)
    private String country;

    @Size(max = 15)
    @Column(name = "zip_code", nullable = false, length = 15)
    private String zipCode;

}