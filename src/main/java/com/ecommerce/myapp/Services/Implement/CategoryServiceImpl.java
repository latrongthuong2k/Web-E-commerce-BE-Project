package com.ecommerce.myapp.Services.Implement;

import com.ecommerce.myapp.DTO.Category.ParentCategoryDto;
import com.ecommerce.myapp.DTO.Category.ReqCreateCategory;
import com.ecommerce.myapp.DTO.Category.ResCategory;
import com.ecommerce.myapp.DTO.Mapper.CategoryMapper;
import com.ecommerce.myapp.Entity.ProductConnectEntites.Category;
import com.ecommerce.myapp.Exceptions.ResourceNotFoundException;
import com.ecommerce.myapp.Repositories.Product.CategoryRepository;
import com.ecommerce.myapp.Services.CategoryService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final CacheManager cacheManager;

    // save new category
    @Override
    public Category saveCategory(ReqCreateCategory reqCreateCategory) {
        String cateName = reqCreateCategory.categoryName();
        Category category = categoryMapper.toEntity(reqCreateCategory);
        return categoryRepository.save(category);
    }

    // Get list all category
    @Override
    public List<ReqCreateCategory> getAllCategory() {
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
            breadCrumb.add(0, category.getCategoryName());
            category = category.getParentCategory();
        }
        return breadCrumb;
    }

    // pagination
    @Override
//    @Cacheable(value = "page_category", key = "#pageable.pageNumber")
    public Page<ReqCreateCategory> getCategoryPage(String query, Pageable pageable) {
//        nameValueCache.add("page_category");
//        System.out.println(cacheManager.getCacheNames());
        Page<Category> category = categoryRepository.findByCategoryNameContainingIgnoreCase(query, pageable);
        return category.map(this::convertToCategoryDto);
    }

    private ReqCreateCategory convertToCategoryDto(Category category) {
        Category parent = category.getParentCategory();
        if (parent != null) {
            ParentCategoryDto parentDto = new ParentCategoryDto(parent.getId(), parent.getCategoryName());
            return new ReqCreateCategory(category.getId(), category.getCategoryName(), parentDto);
        }
        return new ReqCreateCategory(category.getId(), category.getCategoryName(), null);
    }

    @Override
    public void updateCategoryById(Integer categoryId, ReqCreateCategory reqCreateCategory) {
        Category category = foundCategory(categoryId);
        category.setCategoryName(reqCreateCategory.categoryName());
        categoryRepository.save(category);
    }


    @Override
    public void deleteById(Integer id) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if (optionalCategory.isPresent()) {
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
            return new ResCategory(category.getId(), category.getCategoryName(), parentDto);
        }else {
            return new ResCategory(category.getId(), category.getCategoryName(), null);
        }
    }

    @Override
    public Category getCategory(Integer categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(
                () -> new ResourceNotFoundException("No category found with id: " + categoryId));
    }

    //----------------------

    private Category foundCategory(Integer categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(
                () -> new EntityNotFoundException("No category found with id: " + categoryId));
    }
}
