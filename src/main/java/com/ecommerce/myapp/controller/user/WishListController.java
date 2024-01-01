package com.ecommerce.myapp.controller.user;

import com.ecommerce.myapp.dtos.user.mapper.WishListMapper;
import com.ecommerce.myapp.model.client.WishListDto;
import com.ecommerce.myapp.services.UserService;
import com.ecommerce.myapp.services.WistListService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/wish-list")
@PreAuthorize("hasAnyRole('USER','ADMIN','MANAGER')")
public class WishListController {

    private final WistListService wistListService;
    private final WishListMapper wishListMapper;
    private final UserService userService;

    //  /wish-list-add
    @PostMapping("/create")
    public ResponseEntity<Void> createWishList(
            @RequestParam("productId") Long productId
    ) {
        wistListService.createWishList(userService.getCurrentAuditor(), productId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //  /wish-list-get
    @GetMapping("/get")
    public ResponseEntity<List<WishListDto>> getWistList(
    ) {
        List<WishListDto> response = wistListService.getAllWishListOfUser(
                userService.getCurrentAuditor()).stream().map(wishListMapper::toDto).toList();
        return ResponseEntity.ok(response);
    }
    //  /wish-list-delete
    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteItem(
            @RequestParam("productId") Long productId
    ) {
        wistListService.deleteItem(userService.getCurrentAuditor(), productId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}






