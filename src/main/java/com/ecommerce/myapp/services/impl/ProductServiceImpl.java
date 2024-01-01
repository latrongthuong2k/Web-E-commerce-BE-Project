package com.ecommerce.myapp.services.impl;

import com.ecommerce.myapp.dtos.product.ProductFullInfoDTO;
import com.ecommerce.myapp.dtos.product.mappers.ProductFullMapper;
import com.ecommerce.myapp.exceptions.ResourceNotFoundException;
import com.ecommerce.myapp.model.group.Category;
import com.ecommerce.myapp.model.group.Product;
import com.ecommerce.myapp.model.group.ProductImage;
import com.ecommerce.myapp.model.group.ProductSize;
import com.ecommerce.myapp.repositories.ProductImageRepository;
import com.ecommerce.myapp.repositories.ProductRepository;
import com.ecommerce.myapp.repositories.ProductSizeRepository;
import com.ecommerce.myapp.s3.S3Buckets;
import com.ecommerce.myapp.s3.S3ObjectCustom;
import com.ecommerce.myapp.s3.S3Service;
import com.ecommerce.myapp.services.CategoryService;
import com.ecommerce.myapp.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final S3Service s3Service;
    private final S3Buckets s3Buckets;
    private final ProductRepository productRepository;
    private final ProductFullMapper productFullMapper;

    private final CategoryService categoryService;
    private final ProductImageRepository productImageRepository;
    private final ProductSizeRepository productSizeRepository;

    // Services

    @Override
    public void addNewProduct(ProductFullInfoDTO productDto) {
        Product productNew = new Product();
        Category category = categoryService.getCategory(productDto.categoryId());
        Product product = productFullMapper.partialUpdate(productDto, productNew);
        product.setCategory(category);
        Product savedProduct = productRepository.save(product);
        addImages(savedProduct, productDto.imageFiles());
    }

    @Override
    public List<Product> findAllByIds(List<Long> ids) {
        return productRepository.findAllById(ids);
    }

    @Override
    public Product findById(Long productId) {
        return foundProduct(productId);
    }

    @Override
    public Product foundProductByName(String productName) {
        return productRepository.findByProductName(productName).orElseThrow(() -> new ResourceNotFoundException(
                "user with id [%s] not found".formatted(productName)
        ));
    }

    @Override
    public Product getProductById(Long productId) {
        return productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException(
                "Product with ID [%s] not found".formatted(productId)
        ));
    }

    // permitAll

    // search
    @Override
    public List<Product> productsWithSearch(String query) {
        return productRepository.searchAllByProductNameAndDescription(query);
    }

    @Override
    public Page<Product> productPageWithSearch(String query, Pageable pageable) {
        return productRepository.getPageProduct(query, pageable);
    }

    //    @Override
//    public Page<Product> productPageWithSearch(String query, Pageable pageable) {
//        Page<Product> products = productRepository.getPageProduct(query, pageable);
//        return products.map(product -> getProductDetails(product.getId()));
//    }
    @Cacheable(value = "product-details", key = "#productId")
    public Product getProductDetails(Long productId) {
        return productRepository.findById(productId).orElse(null);
    }

    // Selling Products
    @Override
    public Page<Product> getAllProduct(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Override
    public Page<Product> getAllProductByFilter(Long categoryId, BigDecimal price, Set<Long> sizeIDs, Pageable pageable) {
        return productRepository.findAllByFilter(categoryId, price, sizeIDs, pageable);
    }


    // featured-products
    @Override
    public Page<Product> getFeaturedProducts(Pageable pageable) {
        return productRepository.featuredProducts(pageable);
    }

    @Override
    public Page<Product> getNewProducts(Pageable pageable) {
        LocalDate weekAgo = LocalDate.now().minusWeeks(1);
        return productRepository.newProducts(weekAgo, pageable);
    }

    @Override
    public Page<Product> getBestSellerProducts(Pageable pageable) {
        return productRepository.bestSellerProducts(pageable);
    }

    @Override
    public List<ProductSize> getSizes() {
        return productSizeRepository.findAll();
    }
    @Override
    public void deleteById(Long productId) {
        if (productRepository.existsById(productId)) {
            productRepository.deleteById(productId);
        } else
            throw new ResourceNotFoundException(STR."Can't delete product with ID \{productId} because it doesn't exist");

    }


    // Thêm ảnh của sản phẩm
    @Override
    public void addImages(Product product, List<MultipartFile> files) {
        for (MultipartFile file : files) {
            String key = UUID.randomUUID().toString();
            try {
                String imageCode = "product-images/%s/%s".formatted(product.getProductId(), key);
                s3Service.putObject(
                        s3Buckets.getProduct(),
                        imageCode,
                        file.getBytes()
                );
                ProductImage productImage = ProductImage.builder().Key(imageCode).product(product).build();
                productImageRepository.save(productImage);
            } catch (IOException e) {
                throw new RuntimeException(String.format("failed to upload product image with id %s", key), e);
            }
        }
    }

    @Override
    public Set<S3ObjectCustom> getProductImages(Product product) {
        Set<ProductImage> productImages = productImageRepository.findByProduct(product);
        if (productImages.isEmpty()) {
            throw new ResourceNotFoundException(STR."Product with Id \{product} don't have any images");
        }
        List<String> s3Keys = productImages.stream()
                .map(ProductImage::getKey).toList();
        return s3Service.getObjects(s3Buckets.getProduct(), s3Keys);
    }

    @Override
    public Set<S3ObjectCustom> getMainImage(Product product) {
        Optional<ProductImage> productImage = productImageRepository.findMainImage(product);
        List<String> s3Keys = new ArrayList<>();
        productImage.ifPresent(image -> s3Keys.add(image.getKey()));
        return s3Service.getObjects(s3Buckets.getProduct(), s3Keys);
    }

    @Override
    public void deleteS3Image(String imageKey) {
        s3Service.deleteObject(s3Buckets.getProduct(), imageKey);
    }

    @Override
    public void updateProduct(ProductFullInfoDTO updateData) {
        Product product = foundProduct(updateData.productId());
        Product updatedProduct = productFullMapper.partialUpdate(updateData, product);
        if (!updateData.imageFiles().isEmpty()) {
            // Add new images logic
            addImages(product, updateData.imageFiles());
        }
        productRepository.save(updatedProduct);
    }

    private Product foundProduct(Long productId) {
        return productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException(
                "product id [%s] is not found".formatted(productId)
        ));
    }
}
