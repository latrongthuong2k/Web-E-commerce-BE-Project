package com.ecommerce.myapp.repositories;

import com.ecommerce.myapp.model.client.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {
}