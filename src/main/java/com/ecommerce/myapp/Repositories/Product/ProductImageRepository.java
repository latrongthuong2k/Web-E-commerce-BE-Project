package com.ecommerce.myapp.Repositories.Product;

import com.ecommerce.myapp.Entity.ProductConnectEntites.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;


@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Integer> {
    @Query("SELECT i FROM ProductImage i WHERE i.product.id = ?1")
    List<ProductImage> findByProductId(Integer productId);
    @Query("SELECT i FROM ProductImage i WHERE i.id in ?1")
    List<ProductImage> findImagesByProductImageId(Set<Integer> imageIds);
    @Query("SELECT i FROM ProductImage i WHERE i.product.id =?1 AND i.Key =?2")
    Optional<ProductImage> findByKeyAndProductId( Integer productId,String key);
}
