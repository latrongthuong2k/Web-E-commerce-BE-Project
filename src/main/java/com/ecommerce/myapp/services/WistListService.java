package com.ecommerce.myapp.services;

import com.ecommerce.myapp.model.client.ProductWish;
import com.ecommerce.myapp.model.user.AppUser;

import java.util.Set;

public interface WistListService {

    void createWishList(AppUser appUser, Long productId);

    Set<ProductWish> getAllWishListOfUser(AppUser appUser);

    void deleteItem(AppUser appUser, Long productId);
}
