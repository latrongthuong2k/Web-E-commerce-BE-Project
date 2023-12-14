package com.ecommerce.myapp.services.client;

import com.ecommerce.myapp.dto.admin.AppUserDto;

public interface CartService {

    // Tạo một entity Cart
    void createCart(AppUserDto user);

    // Thêm một sản phẩm vào giỏ hàng
    void addToCart(Integer userId, Integer productId, Integer quantity);

    // Xóa một sản phẩm khỏi giỏ hàng
    void removeFromCart(Integer userId, Integer productId);

    // Cập nhật số lượng của một sản phẩm trong giỏ hàng
    void updateCart(Integer userId, Integer productId, Integer quantity);

    // Xem thông tin giỏ hàng

    // Xóa hết các sản phẩm trong giỏ hàng
    void clearCart(Integer userId);

    // Tính tổng giá trị giỏ hàng
    double calculateTotal(Integer userId);

    // Thực hiện thanh toán
    boolean checkout(Integer userId);
}
