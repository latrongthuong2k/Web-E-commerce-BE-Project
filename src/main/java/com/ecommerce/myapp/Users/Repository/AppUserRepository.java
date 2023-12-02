package com.ecommerce.myapp.Users.Repository;

import com.ecommerce.myapp.Users.Entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Integer> {
//    Optional<AppUser> findByUserName(String userName);
    Optional<AppUser> findByEmail(String email);
    @Modifying
    @Query("UPDATE AppUser c SET c.userImage = ?1 WHERE c.id = ?2")
    void updateProfileImageId(String key, Integer userId);
}