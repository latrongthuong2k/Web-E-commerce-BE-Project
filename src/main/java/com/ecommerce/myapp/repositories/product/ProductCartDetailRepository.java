package com.ecommerce.myapp.repositories.product;


import com.ecommerce.myapp.model.product.Product;
import com.ecommerce.myapp.model.client.Cart;
import com.ecommerce.myapp.model.client.ProductCartDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductCartDetailRepository extends JpaRepository<ProductCartDetail, Integer> {
     Optional<ProductCartDetail> findByCartAndProduct(Cart cart, Product product);
}