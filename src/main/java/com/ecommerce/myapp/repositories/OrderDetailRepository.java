package com.ecommerce.myapp.repositories;

import com.ecommerce.myapp.model.checkoutGroup.Order;
import com.ecommerce.myapp.model.checkoutGroup.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
    Set<OrderDetail> findByOrder(Order order);
}