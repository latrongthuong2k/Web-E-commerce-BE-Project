package com.ecommerce.myapp.repositories;

import com.ecommerce.myapp.model.client.UserAddress;
import com.ecommerce.myapp.model.user.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface UserAddressRepository extends JpaRepository<UserAddress, Long> {
    @Query("select u.userAddresses from AppUser u where u = ?1")
    Set<UserAddress> findAllByAppUser(AppUser user);
}