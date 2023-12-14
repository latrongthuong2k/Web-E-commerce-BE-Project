package com.ecommerce.myapp.services.admin;

import com.ecommerce.myapp.dto.product.*;
import com.ecommerce.myapp.model.product.Colors;
import com.ecommerce.myapp.model.product.Product;
import com.ecommerce.myapp.model.product.Sizes;
import com.ecommerce.myapp.model.product.Tags;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public interface ProductService {
    Product addNewProduct(ReqProductDetailDTO createGroup);


    Product foundProductByName(String productName);

    Product getProductById(Integer productId);

    ResProductDetailDTO getProductDetailById(Integer productId);

    List<ProductBasicInfoDTO> getAllProduct();


    Page<ProductBasicInfoDTO> getProductPage(Pageable pageable, String query);

    Product updateProductById(Integer productId, ReqProductDetailDTO product);

    ProductGroupDto getProductGroupDto();

    void updateDiscountInfo(Integer productId, PricingInfoDTO pricingInfoDTO);

    void deleteById(Integer id);

    Page<Product> findFilteredProducts(Integer categoryId,
                                       BigDecimal price,
                                       Set<Colors> colorsIds,
                                       Set<Sizes> sizeIds,
                                       Set<Tags> tagIds,
                                       Pageable pageable);

    void addImages(Integer productId, List<MultipartFile> file, String bucketName);

    List<String> getImagesByID(Integer productId, String bucketName);

    // Xoá ảnh của sản phẩm
    void deleteImage(Integer productId, String imageKey, String bucketName);

    void saveSubmittedImages(Integer productId, List<String> sourceKey, String sourceBucket, String destinationBucket);
}
