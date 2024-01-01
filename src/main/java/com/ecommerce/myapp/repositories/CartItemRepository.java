package com.ecommerce.myapp.repositories;

import com.ecommerce.myapp.model.client.CartItem;
import com.ecommerce.myapp.model.client.ShoppingCart;
import com.ecommerce.myapp.model.group.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.Set;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Set<CartItem> findAllByCart(ShoppingCart shoppingCart);

    @Query("select (count(c) > 0) from CartItem c where c.product.productId = ?1 and c.cart.id = ?2 ")
    boolean existsByProductIdAndCartId(Long productId, Long cartId);

    @Query("select c.product from CartItem c where c = ?1")
    Optional<Product> getProduct(CartItem cartItem);
}