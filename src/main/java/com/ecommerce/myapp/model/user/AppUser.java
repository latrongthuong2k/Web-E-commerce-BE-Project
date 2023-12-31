package com.ecommerce.myapp.model.user;

import com.ecommerce.myapp.model.Review;
import com.ecommerce.myapp.model.ShippingAddress;
import com.ecommerce.myapp.model.WishList;
import com.ecommerce.myapp.model.bill.Bill;
import com.ecommerce.myapp.model.client.Cart;
import com.ecommerce.myapp.security.Token.Token;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
public class AppUser implements UserDetails {

    // id user
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "first_Name", nullable = false)
    private String firstName;

    @Column(name = "last_Name", nullable = false)
    private String lastName;

    // Cột email
    @Column(name = "email", unique = true, nullable = false)
    private String email;

    // Cột password
    @Column(name = "password", unique = true, nullable = false)
    private String password;

    // Thời điểm tạo
    @CreatedDate
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    // Thời điểm update
    @LastModifiedDate
    @Column(name = "update_at", insertable = false)
    private LocalDateTime updateAt;

    @Column(name = "status", nullable = false)
    private Boolean status;

    //---------------- OneToMany ----------------

    @OneToMany(mappedBy = "appUser", cascade = CascadeType.ALL)
    private List<Bill> bills = new ArrayList<>();

    @OneToMany(mappedBy = "appUser", cascade = CascadeType.ALL)
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<ShippingAddress> shippingAddresses = new ArrayList<>();

    @OneToOne(mappedBy = "userId")
    private UserImage userImage;

    //---------------- OneToOne ----------------

    @OneToOne(mappedBy = "appUser", cascade = CascadeType.ALL)
    private Cart cart;
    @OneToOne(mappedBy = "appUser", cascade = CascadeType.ALL)
    private WishList wishLists;


    // impl các phương thức Authenticate

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user")
    private List<Token> tokens;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}