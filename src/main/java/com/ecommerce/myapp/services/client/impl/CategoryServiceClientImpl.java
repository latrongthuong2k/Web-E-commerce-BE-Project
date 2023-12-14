package com.ecommerce.myapp.services.client.impl;

import com.ecommerce.myapp.dto.Mapper.CategoryMapper;
import com.ecommerce.myapp.dto.category.ParentCategoryDto;
import com.ecommerce.myapp.dto.category.ReqClientCategories;
import com.ecommerce.myapp.model.product.Category;
import com.ecommerce.myapp.repositories.CategoryImageRepository;
import com.ecommerce.myapp.repositories.product.CategoryRepository;
import com.ecommerce.myapp.s3.S3Service;
import com.ecommerce.myapp.services.client.CategoryServiceClient;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(rollbackOn = Exception.class)
public class CategoryServiceClientImpl implements CategoryServiceClient {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final CacheManager cacheManager;
    private final S3Service s3Service;
    private final CategoryImageRepository categoryImageRepository;

    @Override
    public List<ReqClientCategories> getClientCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream().map(
                this::convertToCategoryDto).toList();
    }

    //----------------------
    private ReqClientCategories convertToCategoryDto(Category category) {
        Category parent = category.getParentCategory();
        if (parent != null) {
            ParentCategoryDto parentDto = new ParentCategoryDto(parent.getId(), parent.getCategoryName());
            return new ReqClientCategories(category.getId(), category.getCategoryName(), parentDto, category.getImageId());
        }
        return new ReqClientCategories(category.getId(), category.getCategoryName(), null, category.getImageId());
    }

    private Category foundCategory(Integer categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(
                () -> new EntityNotFoundException("No category found with id: " + categoryId));
    }
}
