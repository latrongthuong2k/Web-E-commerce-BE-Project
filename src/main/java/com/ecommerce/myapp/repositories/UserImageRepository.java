package com.ecommerce.myapp.repositories;

import com.ecommerce.myapp.model.user.AppUser;
import com.ecommerce.myapp.model.user.UserImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserImageRepository extends JpaRepository<UserImage, Long> {
    Optional<UserImage> findByAppUser(AppUser appUser);
}