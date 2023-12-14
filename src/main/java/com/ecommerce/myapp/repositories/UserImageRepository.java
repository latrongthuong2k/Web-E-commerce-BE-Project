package com.ecommerce.myapp.repositories;

import com.ecommerce.myapp.model.user.AppUser;
import com.ecommerce.myapp.model.user.UserImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserImageRepository extends JpaRepository<UserImage, Integer> {
    @Query("SELECT i FROM UserImage i WHERE i.userId = :userId")
    Optional<UserImage> findImagesByUserId(@Param("userId") Integer userId);

    boolean existsByUserId(AppUser userId);
}
