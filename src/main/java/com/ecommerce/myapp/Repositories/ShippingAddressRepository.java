package com.ecommerce.myapp.Repositories;

import com.ecommerce.myapp.Entity.ShippingAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShippingAddressRepository extends JpaRepository<ShippingAddress, Integer> {
}