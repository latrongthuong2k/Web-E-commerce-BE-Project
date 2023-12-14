package com.ecommerce.myapp.services.admin.impl;

import com.ecommerce.myapp.dto.Mapper.CategoryMapper;
import com.ecommerce.myapp.dto.category.*;
import com.ecommerce.myapp.exceptions.CannotDeleteException;
import com.ecommerce.myapp.exceptions.DuplicateResourceException;
import com.ecommerce.myapp.exceptions.ResourceNotFoundException;
import com.ecommerce.myapp.model.product.Category;
import com.ecommerce.myapp.model.product.CategoryImage;
import com.ecommerce.myapp.repositories.CategoryImageRepository;
import com.ecommerce.myapp.repositories.product.CategoryRepository;
import com.ecommerce.myapp.s3.S3Buckets;
import com.ecommerce.myapp.s3.S3Service;
import com.ecommerce.myapp.services.admin.CategoryService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@AllArgsConstructor
@Transactional(rollbackOn = Exception.class)
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final CacheManager cacheManager;
    private final S3Service s3Service;
    private final CategoryImageRepository categoryImageRepository;
    private S3Buckets s3Buckets;

    // save new category
    @Override
    public Category saveCategory(ReqCreateCategory reqCreateCategory) {
        Category categoryNew = categoryMapper.toEntity(reqCreateCategory);
        Optional<Category> checkExitsName = categoryRepository.findByCategoryName(reqCreateCategory.categoryName());
        if (checkExitsName.isPresent()) {
            throw new DuplicateResourceException("Category is exist, please input another category name");
        }
        categoryRepository.save(categoryNew);
        return categoryNew;
    }

    // Get list all category
    @Override
    public List<ResCategory> getAllCategory() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream().map(categoryMapper::toDto).toList();
    }

    // BreadCrumb
    @Override
//    @Cacheable(value = "bread_crumb", key = "#categoryId")
    public List<String> getBreadCrumb(Integer categoryId) {
        List<String> breadCrumb = new ArrayList<>();
        Category category = categoryRepository.findById(categoryId).orElse(null);
        while (category != null) {
            // Add at the beginning of the list
            breadCrumb.addFirst(category.getCategoryName());
            category = category.getParentCategory();
        }
        return breadCrumb;
    }

    // pagination
    @Override
    public Page<ReqPageCategory> getCategoryPage(String query, Pageable pageable) {
        Page<Category> category = categoryRepository.findByCategoryNameContainingIgnoreCase(query, pageable);
        return category.map(this::convertToCategoryDto);
    }

    private ReqPageCategory convertToCategoryDto(Category category) {
        Category parent = category.getParentCategory();
        if (parent != null) {
            ParentCategoryDto parentDto = new ParentCategoryDto(parent.getId(), parent.getCategoryName());
            return new ReqPageCategory(category.getId(), category.getCategoryName(), parentDto);
        }
        return new ReqPageCategory(category.getId(), category.getCategoryName(), null);
    }

    @Override
    public void updateCategoryById(Integer categoryId, ReqUpdateCategory reqCreateCategory) {
//        System.out.println("ok");
        Category category = foundCategory(categoryId);
        category.setCategoryName(reqCreateCategory.categoryName());
        if (reqCreateCategory.parentCategory() != null) {
            Category parentCategory = categoryRepository.findById(reqCreateCategory.parentCategory().getId()).orElseThrow(
                    () -> new ResourceNotFoundException("Category to set parent is not found")
            );
            category.setParentCategory(parentCategory);
        } else
            category.setParentCategory(null);
        categoryRepository.save(category);
    }


    @Override
    public void deleteById(Integer id) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        Optional<CategoryImage> optionalCategoryImage = categoryImageRepository.findByCategoryId(id);
        if (optionalCategory.isPresent()) {
            Category category = optionalCategory.get();
            if (!category.getProducts().isEmpty())
                throw new CannotDeleteException("Cannot delete category as it contains products");
            if (optionalCategoryImage.isPresent()) {
                CategoryImage categoryImage = optionalCategoryImage.get();
                deleteImage(id, s3Buckets.getCategoryBucket());
                categoryImageRepository.delete(categoryImage);

            }
            categoryRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("No category found with id: " + id);
        }
    }

    @Override
    public ResCategory findById(Integer categoryId) {
        Category category = foundCategory(categoryId);
        Category parentCategory = category.getParentCategory();
        if (parentCategory != null) {
            ParentCategoryDto parentDto = new ParentCategoryDto(parentCategory.getId(),
                    parentCategory.getCategoryName());
            return new ResCategory(category.getId(), category.getCategoryName(), category.getImageId(), parentDto);
        } else {
            return new ResCategory(category.getId(), category.getCategoryName(), category.getImageId(), null);
        }
    }

    @Override
    public Category getCategory(Integer categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(
                () -> new ResourceNotFoundException("No category found with id: " + categoryId));
    }

    @Override
    public void addImages(Integer categoryId, MultipartFile file, String bucketName) {
        Category category = foundCategory(categoryId);
        CategoryImage categoryImage;
        String key = UUID.randomUUID().toString();
        try {
            String imageCode = "category-images/%s/%s".formatted(categoryId, key);
            s3Service.putObject(
                    bucketName,
                    imageCode,
                    file.getBytes()
            );
            categoryImage = CategoryImage.builder().Key(key).category(category).build();
        } catch (IOException e) {
            throw new RuntimeException(String.format("failed to upload image with category key : %s", key), e);
        }
        categoryImageRepository.save(categoryImage);
    }

    @Override
    public void deleteImage(Integer categoryId, String bucketName) {
        CategoryImage categoryImage = foundCategoryImage(categoryId);
//            try {
//
//            }
//            catch (IOException e) {
//                throw new RuntimeException(String.format("failed to delete image with category key : %s", categoryImage.getKey()), e);
//            }
        String imageCode = "category-images/%s/%s".formatted(categoryId, categoryImage.getKey());
        s3Service.deleteObject(
                bucketName,
                imageCode
        );
        categoryImageRepository.delete(categoryImage);
    }


    @Override
    public String getImageByID(Integer categoryId, String bucketName) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category id " + categoryId + " is not"));
        CategoryImage categoryImage = categoryImageRepository.findByCategory(category).orElseThrow(
                () -> new ResourceNotFoundException("CategoryImage entity with category Id : " + categoryId + ", is not found")
        );

        // S3のためのキーリストを作成する
        String s3Key = "category-images/%s/%s".formatted(categoryId, categoryImage.getKey());
        // S3からURLを取得する
        return s3Service.getObjectUrl(bucketName, s3Key);
    }

    //----------------------

    private Category foundCategory(Integer categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(
                () -> new ResourceNotFoundException("No category found with id: " + categoryId));
    }

    private CategoryImage foundCategoryImage(Integer categoryId) {
        return categoryImageRepository.findByCategoryId(categoryId).orElseThrow(
                () -> new ResourceNotFoundException("No categoryImage found with id: " + categoryId));
    }
}
