package com.ecommerce.myapp.services.client;

import com.ecommerce.myapp.dto.category.ReqClientCategories;

import java.util.List;


public interface CategoryServiceClient {
    List<ReqClientCategories> getClientCategories();
}
