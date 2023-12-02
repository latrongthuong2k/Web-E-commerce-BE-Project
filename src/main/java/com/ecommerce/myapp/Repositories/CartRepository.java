package com.ecommerce.myapp.Repositories;

import com.ecommerce.myapp.Entity.ShopingCart.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {
}