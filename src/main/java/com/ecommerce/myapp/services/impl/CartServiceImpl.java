package com.ecommerce.myapp.services.impl;

import com.ecommerce.myapp.dtos.product.request.ReqProductOnCart;
import com.ecommerce.myapp.exceptions.ResourceNotFoundException;
import com.ecommerce.myapp.model.client.CartItem;
import com.ecommerce.myapp.model.client.ShoppingCart;
import com.ecommerce.myapp.model.group.Product;
import com.ecommerce.myapp.model.user.AppUser;
import com.ecommerce.myapp.repositories.CartItemRepository;
import com.ecommerce.myapp.repositories.ShoppingCartRepository;
import com.ecommerce.myapp.services.CartService;
import com.ecommerce.myapp.services.OrderService;
import com.ecommerce.myapp.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartItemRepository cartItemRepository;

    private final ProductService productService;
    private final OrderService orderService;
    private final ShoppingCartRepository shoppingCartRepository;

    @Override
    public ShoppingCart getUserCart(AppUser appUser) {
        Optional<ShoppingCart> userCart = shoppingCartRepository.findByAppUser(appUser);
        if (userCart.isPresent()) {
            return userCart.get();
        } else {
            ShoppingCart shoppingCart = new ShoppingCart();
            shoppingCart.setAppUser(appUser);
            return shoppingCartRepository.save(shoppingCart);
        }
    }

    @Override
    public void createCart(AppUser appUser) {
        Optional<ShoppingCart> userCart = shoppingCartRepository.findByAppUser(appUser);
        if (userCart.isEmpty()) {
            ShoppingCart shoppingCart = new ShoppingCart();
            shoppingCart.setAppUser(appUser);
            shoppingCartRepository.save(shoppingCart);
        }
    }

    @Override
    public Set<Product> getAllProduct(AppUser appUser) {
        var userCart = getUserCart(appUser);
        Set<Product> products = new HashSet<>();
        Set<CartItem> cartItems = cartItemRepository.findAllByCart(userCart);
        cartItems.forEach(cartItem -> {
            cartItemRepository.getProduct(cartItem).ifPresent(products::add);
        });
        return products;
    }

    @Override
    public void addProductToCart(AppUser user, List<ReqProductOnCart> productsOnCart) {
        ShoppingCart userCart = getUserCart(user);
        Set<CartItem> cartItems = cartItemRepository.findAllByCart(userCart);
        List<Long> productIds = productsOnCart.stream()
                .map(ReqProductOnCart::productId)
                .toList();
        List<Product> products = productService.findAllByIds(productIds);

        Map<Long, Product> productMap = products.stream()
                .collect(Collectors.toMap(Product::getProductId, product -> product));

        for (ReqProductOnCart productReq : productsOnCart) {
            Product product = productMap.get(productReq.productId());
            if (product != null) {
                CartItem cartItem = new CartItem();
                cartItem.setProduct(product);
                cartItem.setCart(userCart);
                cartItem.setQuantity(productReq.quantity());
                cartItems.add(cartItem);
            } else {
                throw new ResourceNotFoundException(STR."Product with ID \{productReq.productId()} cannot found !");
            }
        }
        cartItemRepository.saveAll(cartItems);
    }


    @Override
    public void updateCartItem(AppUser user, Long cartItemId, Integer quantity) {
        Set<CartItem> cartItems = getCartItems(user);
        cartItems.stream()
                .filter(item -> item.getId().equals(cartItemId))
                .findFirst()
                .ifPresent(item ->
                        item.setQuantity(quantity));
        cartItemRepository.saveAll(cartItems);
    }

    @Override
    public void deleteCartItem(AppUser user, Long cartItemId) {
        Set<CartItem> cartItems = getCartItems(user);
        cartItems.removeIf(item -> item.getId().equals(cartItemId));
        cartItemRepository.saveAll(cartItems);
    }

    @Override
    public Set<CartItem> getCartItems(AppUser user) {
        ShoppingCart userCart = getUserCart(user);
        return cartItemRepository.findAllByCart(userCart);
    }

    @Override
    public void clearAllItem(AppUser user) {
        Set<CartItem> cartItems = getCartItems(user);
        if (!cartItems.isEmpty()) {
            cartItems.clear();
            cartItemRepository.saveAll(cartItems);
        } else
            throw new ResourceNotFoundException("Currently don't have any item on cart !");
    }

    @Override
    public void checkOut(AppUser user, String note) {
        orderService.createOrder(user, note, getCartItems(user));
    }
}
