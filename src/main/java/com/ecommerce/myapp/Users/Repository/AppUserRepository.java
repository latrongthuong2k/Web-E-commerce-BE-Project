package com.ecommerce.myapp.Users.Repository;

import com.ecommerce.myapp.Users.Entity.AppUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Query("select a from AppUser a where" +
           " lower(a.email) like lower(concat('%', ?1, '%')) " +
           "or lower(a.firstName) like lower(concat('%', ?1, '%')) " +
           "or lower(a.lastName) like lower(concat('%', ?1, '%')) " +
           "or cast(a.createdAt as STRING ) like lower(concat('%', ?1, '%'))")
    Page<AppUser> findBySearch(String search, Pageable pageable);
}