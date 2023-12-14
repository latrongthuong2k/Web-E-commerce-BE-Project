package com.ecommerce.myapp.services.admin;

import com.ecommerce.myapp.dto.category.ReqCreateCategory;
import com.ecommerce.myapp.dto.category.ReqPageCategory;
import com.ecommerce.myapp.dto.category.ReqUpdateCategory;
import com.ecommerce.myapp.dto.category.ResCategory;
import com.ecommerce.myapp.model.product.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface CategoryService {
    Category saveCategory(ReqCreateCategory reqCreateCategory);

    List<ResCategory> getAllCategory();

    List<String> getBreadCrumb(Integer categoryId);

    // phân trang
    Page<ReqPageCategory> getCategoryPage(String query, Pageable pageable);

    void updateCategoryById(Integer categoryId, ReqUpdateCategory reqCreateCategory);

    void deleteById(Integer id);

    ResCategory findById(Integer categoryId);
    Category getCategory(Integer categoryId);

    void addImages(Integer categoryId, MultipartFile files, String bucketName);

    void deleteImage(Integer categoryId, String bucketName);

    String getImageByID(Integer categoryId, String bucketName);
}
