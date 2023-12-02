package com.ecommerce.myapp.Repositories.Product;

import com.ecommerce.myapp.Entity.ProductConnectEntites.TermImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface TermImageRepository extends JpaRepository<TermImage, Integer> {
    @Query("SELECT i FROM TermImage i WHERE i.product.id = ?1")
    List<TermImage> findByProductId(Integer productId);
    @Query("SELECT i FROM TermImage i WHERE i.id in ?1")
    List<TermImage> findImagesByProductImageId(Set<Integer> imageIds);
    @Query("SELECT i FROM TermImage i WHERE i.product.id =?1 AND i.Key =?2")
    Optional<TermImage> findByKeyAndProductId(String key, Integer productId);
}