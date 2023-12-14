package com.ecommerce.myapp.controllers.client;

import com.ecommerce.myapp.dto.category.ReqClientCategories;
import com.ecommerce.myapp.s3.S3Buckets;
import com.ecommerce.myapp.services.client.CategoryServiceClient;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/client/category")
public class CategoryControllerClient {
    private final CategoryServiceClient categoryService;
    private final S3Buckets s3Buckets;

    //    @Cacheable(value = "categoryPage", key = "{#page,#sortField,#sortDir,#query}")
//    @GetMapping("/page")
//    public ResponseEntity<Map<String, Object>> getCategoryPage(
//            @RequestParam(name = "page", defaultValue = "0") int page,
//            @RequestParam(name = "size", defaultValue = "20") int size,
//            @RequestParam(name = "q", defaultValue = "") String query,
//            @RequestParam(name = "sortField", defaultValue = "categoryName") String sortField,
//            @RequestParam(name = "sortDir", defaultValue = "asc") String sortDir) {
//        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDir.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortField));
//        Page<ReqPageCategory> categories = categoryService.getCategoryPage(query, pageable);
//        Map<String, Object> response = new HashMap<>();
//        response.put("categories", categories.getContent());
////        response.put("currentPage", productPage.getNumber());
////        response.put("totalItems", productPage.getTotalElements());
//        response.put("totalPages", categories.getTotalPages());
//        return ResponseEntity.ok(response);
//    }
    @GetMapping("/categories")
    public ResponseEntity<List<ReqClientCategories>> getCategories() {
        return ResponseEntity.ok(categoryService.getClientCategories());
    }
//    @GetMapping("/get-images/{id}")
//    public String getImageUrl(@PathVariable("id") Integer productId) {
//        // Key và url trả về
//        return categoryService.getImageByID(productId, s3Buckets.getCategoryBucket());
//    }
}
