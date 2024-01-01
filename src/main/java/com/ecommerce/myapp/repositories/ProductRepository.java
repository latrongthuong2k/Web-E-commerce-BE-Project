package com.ecommerce.myapp.repositories;


import com.ecommerce.myapp.model.group.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT p FROM Product p WHERE " +
           "LOWER(p.productName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(p.description) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<Product> searchAllByProductNameAndDescription(String search);

    @Query("SELECT p FROM Product p WHERE " +
           "LOWER(p.productName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(p.description) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Product> getPageProduct(String search, Pageable pageable);

    Optional<Product> findByProductName(String productName);

    // featured-products
    @Query("SELECT p FROM Product p ORDER BY SIZE(p.productWishes)")
    Page<Product> featuredProducts(Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.createdAt >= :weekAgo")
    Page<Product> newProducts(LocalDate weekAgo, Pageable pageable);

    @Query("SELECT p FROM Product p ORDER BY SIZE(p.orderDetails)")
    Page<Product> bestSellerProducts(Pageable pageable);

    @Query(value = """
            SELECT p.* FROM `ecommerce-test-db`.products p
            left join `ecommerce-test-db`.product_sizes ps
            ON p.product_id = ps.prod_id
            WHERE (:categoryId IS NULL OR p.category_id = :categoryId)
            AND (:price IS NULL OR p.unit_price <= :price OR (:price = 0 AND p.unit_price > 0))
            AND (:sizeIDs IS NULL OR ps.size_id IN :sizeIDs)
            """, nativeQuery = true)
    Page<Product> findAllByFilter(@Param("categoryId") Long categoryId,
                                  @Param("price") BigDecimal price,
                                  @Param("sizeIDs") Set<Long> sizeIDs,
                                  Pageable pageable);
}