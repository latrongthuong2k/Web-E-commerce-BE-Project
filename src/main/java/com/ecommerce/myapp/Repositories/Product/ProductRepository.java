package com.ecommerce.myapp.Repositories.Product;

import com.ecommerce.myapp.DTO.Product.ProductBasicInfoDTO;
import com.ecommerce.myapp.Entity.ProductConnectEntites.Colors;
import com.ecommerce.myapp.Entity.ProductConnectEntites.Product;
import com.ecommerce.myapp.Entity.ProductConnectEntites.Sizes;
import com.ecommerce.myapp.Entity.ProductConnectEntites.Tags;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    // Find product from category father
    // Common Table Expressions (CTE): / hoặc cách khác là có thể sủ dụng {Queue : hàng đợi} hoặc {đệ quy} ở cấp ứng dụng
    @Query(value = """
            WITH RECURSIVE CategoryPath AS (
                SELECT id
                FROM `web-e-commerce-db`.category
                WHERE id = :categoryId
                UNION ALL
                SELECT c.id
                FROM `web-e-commerce-db`.category c
                INNER JOIN CategoryPath cp ON c.parent_id = cp.id
            )
            SELECT p.*
            FROM `web-e-commerce-db`.products p
            LEFT JOIN `web-e-commerce-db`.product_color pc ON p.id = pc.product_id
            LEFT JOIN `web-e-commerce-db`.product_size ps ON p.id = ps.product_id
            LEFT JOIN `web-e-commerce-db`.product_tag pt ON p.id = pt.product_id
            WHERE p.category_id IN (SELECT id FROM CategoryPath)
            AND pc.color_id IN :colorsIds
            AND ps.size_id IN :sizeIds
            AND pt.tag_id IN :tagIds
            AND (p.price BETWEEN 0 AND IF(:price = 0, (SELECT MAX(price) FROM `web-e-commerce-db`.products), :price))
             
            """, nativeQuery = true)
    Page<Product> findProductsInCategoryAndSubCategoriesOrByFilters(
            @Param("categoryId") Integer categoryId,
            @Param("price") BigDecimal price,
            @Param("colorsIds") Set<Colors> colorsIds,
            @Param("sizeIds") Set<Sizes> sizeIds,
            @Param("tagIds") Set<Tags> tagIds,
            Pageable pageable
    );

    @Query("SELECT new com.ecommerce.myapp.DTO.Product.ProductBasicInfoDTO(p.id, p.productName, p.createdAt, p.updatedAt, " +
           "p.inventory.stockQuantity, p.price, p.status) FROM Product p WHERE " +
           "LOWER(p.productName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "CAST(p.price AS string) LIKE LOWER(CONCAT('%', :search, '%')) or " +
           "CAST(p.createdAt AS string) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<ProductBasicInfoDTO> findByProductNameOrCreatedAtOrPrice(@Param("search") String search, Pageable pageable);

    @Query("SELECT new com.ecommerce.myapp.DTO.Product.ProductBasicInfoDTO(p.id, p.productName, p.createdAt, p.updatedAt, " +
           "p.inventory.stockQuantity, p.price, p.status) FROM Product p")
    Page<ProductBasicInfoDTO> findAllProduct(Pageable pageable);

    Optional<Product> findByProductName(String productName);
}