package com.ecommerce.myapp.repositories;

import com.ecommerce.myapp.model.client.ShoppingCart;
import com.ecommerce.myapp.model.user.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    Optional<ShoppingCart> findByAppUser(AppUser appUser);
}