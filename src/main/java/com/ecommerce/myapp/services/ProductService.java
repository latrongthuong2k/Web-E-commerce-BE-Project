package com.ecommerce.myapp.services;

import com.ecommerce.myapp.dtos.product.ProductFullInfoDTO;
import com.ecommerce.myapp.model.group.Product;
import com.ecommerce.myapp.model.group.ProductSize;
import com.ecommerce.myapp.s3.S3ObjectCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public interface ProductService {
    void addNewProduct(ProductFullInfoDTO newProduct);
    List<Product> findAllByIds(List<Long> ids);

    Product findById(Long productId);

    Product foundProductByName(String productName);
    Product getProductById(Long productId);
    List<Product> productsWithSearch(String query);
    Page<Product> productPageWithSearch(String query, Pageable pageable);
    // many data
    Page<Product> getAllProduct(Pageable categoryId);

    Page<Product> getAllProductByFilter(Long categoryId, BigDecimal price, Set<Long> sizeIDs, Pageable pageable);

    Page<Product> getFeaturedProducts(Pageable pageable);
    Page<Product> getNewProducts(Pageable pageable);
    Page<Product> getBestSellerProducts(Pageable pageable);

    List<ProductSize> getSizes();

    void deleteById(Long id);
    void addImages(Product product, List<MultipartFile> files);
    Set<S3ObjectCustom> getProductImages(Product product);

    Set<S3ObjectCustom> getMainImage(Product product);

    void deleteS3Image(String imageKey);
    void updateProduct(ProductFullInfoDTO updateData);
}
