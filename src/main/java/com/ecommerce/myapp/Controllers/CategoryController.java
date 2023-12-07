package com.ecommerce.myapp.Controllers;

import com.ecommerce.myapp.DTO.Category.ReqCreateCategory;
import com.ecommerce.myapp.DTO.Category.ReqUpdateCategory;
import com.ecommerce.myapp.DTO.Category.ResCategory;
import com.ecommerce.myapp.Entity.ProductConnectEntites.Category;
import com.ecommerce.myapp.Exceptions.DuplicateResourceException;
import com.ecommerce.myapp.Services.CategoryService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/category")
public class CategoryController {
    private final CategoryService categoryService;
//    private final CacheManager cacheManager;

    @CacheEvict(value = "categoryPage", allEntries = true)
    @PostMapping("create")
    public ResponseEntity<String> createCategory( @Valid @RequestBody ReqCreateCategory reqCreateCategory) {
        categoryService.saveCategory(reqCreateCategory);
        return ResponseEntity.ok("Category created successfully");
    }
    @CacheEvict(value = "categoryPage", allEntries = true)
    @PutMapping("/update")
    public ResponseEntity<String> updateCategory(@RequestParam("categoryId") Integer categoryId, @Valid @RequestBody ReqUpdateCategory reqCreateCategory) {
        categoryService.updateCategoryById(categoryId, reqCreateCategory);
        return ResponseEntity.ok().body("Category updated successfully");
    }

    //    @GetMapping("/{categoryId}")
//    public ResponseEntity<String> getUserById(@PathVariable Integer categoryId) {
//        ReqCreateCategory reqCreateCategory = categoryService.findById(categoryId);
//        return ResponseEntity.ok().body("Category found: " + reqCreateCategory.categoryName());
//    }
    @GetMapping("/get")
    public ResponseEntity<ResCategory> getCategoryById(@RequestParam(name = "categoryId") Integer id) {
        return ResponseEntity.ok(categoryService.findById(id));
    }


    @GetMapping("/list-category")
    public ResponseEntity<List<ReqCreateCategory>> getAllCategory() {
        List<ReqCreateCategory> categories = categoryService.getAllCategory();
        return ResponseEntity.ok(categories);
    }

    @Cacheable(value = "categoryPage", key = "{#page,#sortField,#sortDir,#query}")
    @GetMapping("/page")
    public ResponseEntity<Map<String, Object>> getCategoryPage(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size,
            @RequestParam(name = "q", defaultValue = "") String query,
            @RequestParam(name = "sortField", defaultValue = "categoryName") String sortField,
            @RequestParam(name = "sortDir", defaultValue = "asc") String sortDir) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDir.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortField));
        Page<ReqCreateCategory> categories = categoryService.getCategoryPage(query, pageable);
        Map<String, Object> response = new HashMap<>();
        response.put("categories", categories.getContent());
//        response.put("currentPage", productPage.getNumber());
//        response.put("totalItems", productPage.getTotalElements());
        response.put("totalPages", categories.getTotalPages());
        return ResponseEntity.ok(response);
    }

    @CacheEvict(value = "categoryPage", allEntries = true)
    @DeleteMapping("/delete/{categoryId}")
    public ResponseEntity<String> deleteCategory(@PathVariable Integer categoryId) {
        categoryService.deleteById(categoryId);
        return ResponseEntity.ok().body("Category deleted successfully");
    }
}
