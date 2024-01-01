package com.ecommerce.myapp.services.impl;

import com.ecommerce.myapp.model.client.ProductWish;
import com.ecommerce.myapp.model.user.AppUser;
import com.ecommerce.myapp.repositories.WishListRepository;
import com.ecommerce.myapp.services.ProductService;
import com.ecommerce.myapp.services.UserService;
import com.ecommerce.myapp.services.WistListService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class WishListServiceImpl implements WistListService {
    private final ProductService productService;
    private final UserService userService;

    private final WishListRepository wishListRepository;

    @Override
    public void createWishList(AppUser appUser, Long productId) {
        var product = productService.getProductById(productId);
        Set<ProductWish> userProductWishes = getWishLists(appUser);
        ProductWish productWish = new ProductWish();
        productWish.setProduct(product);
        userProductWishes.add(productWish);
        wishListRepository.saveAll(userProductWishes);
    }

    private Set<ProductWish> getWishLists(AppUser appUser) {
        return wishListRepository.findAllByAppUser(appUser);
    }

    @Override
    public Set<ProductWish> getAllWishListOfUser(AppUser appUser) {
        return appUser.getProductWish();
    }

    @Override
    public void deleteItem(AppUser appUser, Long productWishId) {
        Set<ProductWish> userProductWishes = getWishLists(appUser);
        userProductWishes.removeIf(wish -> wish.getId().equals(productWishId));
        wishListRepository.saveAll(userProductWishes);
    }
}
