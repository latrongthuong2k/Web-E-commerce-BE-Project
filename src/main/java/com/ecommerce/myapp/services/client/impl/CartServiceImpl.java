package com.ecommerce.myapp.services.client.impl;

import com.ecommerce.myapp.dto.admin.AppUserDto;
import com.ecommerce.myapp.model.client.Cart;
import com.ecommerce.myapp.model.client.ProductCartDetail;
import com.ecommerce.myapp.model.product.Product;
import com.ecommerce.myapp.model.user.AppUser;
import com.ecommerce.myapp.repositories.AppUserRepository;
import com.ecommerce.myapp.repositories.CartRepository;
import com.ecommerce.myapp.repositories.product.ProductCartDetailRepository;
import com.ecommerce.myapp.repositories.product.ProductRepository;
import com.ecommerce.myapp.services.client.CartService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional(rollbackOn = Exception.class)
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final AppUserRepository appUserRepository;
    private final ProductRepository productRepository;
    private final ProductCartDetailRepository productCartDetailRepository;

    public CartServiceImpl(CartRepository cartRepository,
                           AppUserRepository appUserRepository,
                           ProductRepository productRepository,
                           ProductCartDetailRepository productCartDetailRepository) {
        this.cartRepository = cartRepository;
        this.appUserRepository = appUserRepository;
        this.productRepository = productRepository;
        this.productCartDetailRepository = productCartDetailRepository;
    }

    @Override
    public void createCart(AppUserDto user) {
        Cart cart = new Cart();
        cartRepository.save(cart);
    }


    @Override
    public void addToCart(Integer userId, Integer productId, Integer quantity) {
        // Check sự tồn tại của product trong cart và thêm hoặc cập nhật
        updateCart(userId, productId, quantity);
    }

    ///

    @Override
    public void removeFromCart(Integer userId, Integer productId) {
        AppUser user = foundUser(userId);
        user.getCart().getProductCartDetails().removeIf(productCartDetail
                -> productCartDetail.getProduct().getId().equals(productId));
        cartRepository.save(user.getCart());
    }

    @Override
    public void updateCart(Integer userId, Integer productId, Integer quantity) {
        AppUser user = foundUser(userId);
        Product product = foundProduct(productId);

        // Tìm productCartDetail trong cơ sở dữ liệu
        Optional<ProductCartDetail> foundProductCartDetail = productCartDetailRepository.findByCartAndProduct(user.getCart(), product);
        if (foundProductCartDetail.isPresent()) {
            // Nếu tìm thấy, cập nhật quantity
           ProductCartDetail productCartDetail = foundProductCartDetail.get();
            productCartDetail.setQuantity(quantity);
            productCartDetailRepository.save(productCartDetail);
        } else {
            // Nếu không tìm thấy, tạo một đối tượng mới
            ProductCartDetail newProductCartDetail = new ProductCartDetail();
            newProductCartDetail.setCart(user.getCart());
            newProductCartDetail.setProduct(product);
            newProductCartDetail.setQuantity(quantity);
            user.getCart().getProductCartDetails().add(newProductCartDetail);
            productCartDetailRepository.save(newProductCartDetail);
        }
    }
    @Override
    public void clearCart(Integer userId) {
        AppUser user = foundUser(userId);
        user.getCart().getProductCartDetails().clear();
        cartRepository.save(user.getCart());
    }

    @Override
    public double calculateTotal(Integer userId) {
        return 0;
    }

    @Override
    public boolean checkout(Integer userId) {
        return false;
    }

    // custom found

    private AppUser foundUser(Integer userId) {
        return appUserRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException("Không tìm thấy user"));
    }

    private Product foundProduct(Integer productId) {
        return productRepository.findById(productId).orElseThrow(
                () -> new EntityNotFoundException("Không tìm thấy product"));
    }
}