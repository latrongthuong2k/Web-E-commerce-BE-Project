package com.ecommerce.myapp.Controllers;

import com.ecommerce.myapp.DTO.Product.ProductBasicInfoDTO;
import com.ecommerce.myapp.DTO.Product.ReqProductDetailDTO;
import com.ecommerce.myapp.DTO.Product.ResProductDetailDTO;
import com.ecommerce.myapp.DTO.Product.ResViewProductDTO;
import com.ecommerce.myapp.Entity.ProductConnectEntites.*;
import com.ecommerce.myapp.Services.ProductService;
import com.ecommerce.myapp.s3.S3Buckets;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/product")
public class ProductController {

    private final ProductService productService;
    //    private final InventoryService inventoryService;
    private final CacheManager cacheManager;
    private final S3Buckets s3Buckets;

    // GET
    @Cacheable(value = "productPage", key = "{#page,#sortField,#sortDir}")
    @GetMapping("/page")
    public ResponseEntity<Map<String, Object>> getPageProduct(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size,
            @RequestParam(name = "q", defaultValue = "") String query,
            @RequestParam(name = "sortField", defaultValue = "productName") String sortField,
            @RequestParam(name = "sortDir", defaultValue = "asc") String sortDir) {
        // nếu query không null sủ lý gọi hàm học theo key
//        Objects.requireNonNull(cacheManager.getCache("productPage")).clear();
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDir.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortField));
        Page<ProductBasicInfoDTO> productPage = productService.getProductPage(pageable, query);
        // Trả về object mới
        Map<String, Object> response = new HashMap<>();
        response.put("products", productPage.getContent());
        // response.put("currentPage", productPage.getNumber());
        // response.put("totalItems", productPage.getTotalElements());
        response.put("totalPages", productPage.getTotalPages());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get")
    public ResponseEntity<ResProductDetailDTO> getPageProduct(@RequestParam(value = "productId") Integer id) {
        ResProductDetailDTO product = productService.getProductDetailById(id);
        return ResponseEntity.ok(product);
    }


    @GetMapping("/filter-products")
    public ResponseEntity<List<ResViewProductDTO>> getFilterProductPage(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size,
            @RequestParam(name = "categoryId") Integer categoryId,
            @RequestParam(name = "price", defaultValue = "0") BigDecimal price,
            @RequestParam(name = "colorsIds") Set<Colors> colorsIds,
            @RequestParam(name = "sizeIds") Set<Sizes> sizeIds,
            @RequestParam(name = "tagIds") Set<Tags> tagIds) {
        Pageable pageable = PageRequest.of(page, size);
        // Get page product
        Page<Product> products = productService.findFilteredProducts(categoryId, price, colorsIds, sizeIds, tagIds, pageable);
        // Mapper to responseList
        List<ResViewProductDTO> responseList = products.stream()
                .map(product -> new ResViewProductDTO(product.getProductName(),
                        product.getDescription(),
                        product.getPrice(),
                        product.getClientType(),
                        product.getColors(),
                        product.getSizes(),
                        product.getTags(),
                        product.getImages())).toList();
        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/product-connected-entities")
    public ResponseEntity<?> getProductById() {
        return ResponseEntity.ok(productService.getEntitiesConnectedToProduct());
    }

    // PUT
    @CacheEvict(value = "productPage", allEntries = true)
    @PutMapping("/update")
    public ResponseEntity<Integer> updateProduct(
            @RequestParam(name = "productId") Integer productId,
            @Valid @RequestBody ReqProductDetailDTO product) {
        return ResponseEntity.ok(productService.updateProductById(productId, product).getId());
    }

    // POST
    // Todo : nếu tạo một sản phẩm, thì xoá caching, còn gọi bth thì cứ catching thoải mái
    @CacheEvict(value = "productPage", allEntries = true)
    @PostMapping(value = "/create")
    public ResponseEntity<Integer> createProduct(
            @RequestBody @Valid ReqProductDetailDTO createProduct
    ) {
        Product product = productService.addNewProduct(createProduct);
        return ResponseEntity.ok(product.getId());
    }

    //     POST /term-product-image
    @PostMapping(value = "/product-images/{productId}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void addImages(
            @PathVariable("productId") Integer productId,
            @RequestPart("file") List<MultipartFile> files) {
        productService.addImages(productId, files, s3Buckets.getProduct());
    }

    //     GET /term-profile-image
    @GetMapping("/product-images/{id}")
    public Map<String, String> getTermImages(@PathVariable("id") Integer productId) {
        // Key và url trả về
        return productService.getImagesByID(productId, s3Buckets.getProduct());
    }

    @DeleteMapping("/product-images")
    public ResponseEntity<?> getTermImages(@RequestParam(name = "productId") Integer productId,
                                           @RequestParam(name = "imageKey") String imageKey) {
        productService.deleteImage(productId, imageKey, s3Buckets.getProduct());
        return ResponseEntity.ok("ok");
    }

//    @PostMapping(value = "/product-images",
//            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public void addImages(
//            @RequestPart("file") List<MultipartFile> files) {
//        productService.addImages(6, files, s3Buckets.getProduct());
//    }

    //     GET / profile-image
    @GetMapping(
            value = "{productId}/product-images",
            produces = MediaType.IMAGE_JPEG_VALUE
    )
    public Map<String, String> getImages(@PathVariable("productId") Integer productId) {
        return productService.getImagesByID(productId, s3Buckets.getProduct());
    }


    // DELETE
    @CacheEvict(value = "productPage", allEntries = true)
    @DeleteMapping("/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable Integer productId) {
        productService.deleteById(productId);
        return ResponseEntity.ok().body("Product deleted successfully");
    }

}






