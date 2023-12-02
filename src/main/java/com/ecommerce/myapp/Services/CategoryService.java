package com.ecommerce.myapp.Services;

import com.ecommerce.myapp.DTO.Category.ReqCreateCategory;
import com.ecommerce.myapp.DTO.Category.ResCategory;
import com.ecommerce.myapp.Entity.ProductConnectEntites.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface CategoryService {
    Category saveCategory(ReqCreateCategory reqCreateCategory);

    List<ReqCreateCategory> getAllCategory();

    List<String> getBreadCrumb(Integer categoryId);

    // phân trang
    Page<ReqCreateCategory> getCategoryPage(String query, Pageable pageable);

    void updateCategoryById(Integer categoryId, ReqCreateCategory reqCreateCategory);

    void deleteById(Integer id);

    ResCategory findById(Integer categoryId);
    Category getCategory(Integer categoryId);

}
