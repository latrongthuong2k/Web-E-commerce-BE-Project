package com.ecommerce.myapp.repositories;

import com.ecommerce.myapp.model.client.ProductWish;
import com.ecommerce.myapp.model.user.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface WishListRepository extends JpaRepository<ProductWish, Long> {
    Set<ProductWish> findAllByAppUser(AppUser appUser);
}