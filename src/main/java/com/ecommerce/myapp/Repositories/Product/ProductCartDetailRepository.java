package com.ecommerce.myapp.Repositories.Product;


import com.ecommerce.myapp.Entity.ProductConnectEntites.Product;
import com.ecommerce.myapp.Entity.ShopingCart.Cart;
import com.ecommerce.myapp.Entity.ShopingCart.ProductCartDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductCartDetailRepository extends JpaRepository<ProductCartDetail, Integer> {
     Optional<ProductCartDetail> findByCartAndProduct(Cart cart, Product product);
}