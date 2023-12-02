package com.ecommerce.myapp.Controllers;


import com.ecommerce.myapp.DTO.ProductCartDetailDDTO;
import com.ecommerce.myapp.DTO.UserWithProductDTO;
import com.ecommerce.myapp.Services.CartService;
import com.ecommerce.myapp.Users.HttpReqRes.AppUserDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    // POST
    @PostMapping("/create")
    public ResponseEntity<String> createCart(@Valid @RequestBody AppUserDto user) {
        //        cartService.createCart(user);
        return new ResponseEntity<>("Cart created", HttpStatus.CREATED);
    }
    // POST
    @PostMapping("/create1")
    public ResponseEntity<String> createCart() {
//        cartService.createCart(user);
        return new ResponseEntity<>("Cart created", HttpStatus.CREATED);
    }

    // POST
    @PostMapping("/add")
    public ResponseEntity<String> addToCart(@Valid @RequestBody UserWithProductDTO userWithProductDto) {
        cartService.addToCart(userWithProductDto.userId(), userWithProductDto.productId(),
                userWithProductDto.quantity());
        return ResponseEntity.ok("Product added to cart successfully");
    }

    // POST
    @PostMapping("/remove")
    public ResponseEntity<String> removeFromCart(@Valid @RequestBody UserWithProductDTO userWithProductDto) {
        cartService.removeFromCart(userWithProductDto.userId(), userWithProductDto.productId());
        return ResponseEntity.ok("Product removed from cart successfully");
    }

    // POST
    @PostMapping("/update")
    public ResponseEntity<String> updateCart(@Valid @RequestBody UserWithProductDTO userWithProductDto) {
        cartService.updateCart(userWithProductDto.userId(), userWithProductDto.productId(),
                userWithProductDto.quantity());
        return ResponseEntity.ok("Cart updated successfully");
    }

    // GET
    @GetMapping("/view/{userId}")
    public ResponseEntity<List<ProductCartDetailDDTO>> viewCart(@PathVariable Integer userId) {
        return ResponseEntity.ok(cartService.viewCart(userId));
    }

    // POST
    @PostMapping("/clear/{userId}")
    public ResponseEntity<String> clearCart(@PathVariable Integer userId) {
        cartService.clearCart(userId);
        return ResponseEntity.ok("Cart cleared successfully");
    }
}
