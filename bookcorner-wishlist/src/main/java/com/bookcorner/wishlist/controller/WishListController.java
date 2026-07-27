package com.bookcorner.wishlist.controller;


import com.bookcorner.wishlist.dto.WishlistResponse;
import com.bookcorner.wishlist.service.WishListImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishlists")
@RequiredArgsConstructor
public class WishListController {

    private final WishListImpl wishListService;

    @PostMapping("/{bookId}")
    public ResponseEntity<WishlistResponse> addBookToWishlist(@PathVariable Long bookId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(wishListService.addBookToWishlist(bookId));
    }

    @GetMapping
    public ResponseEntity<List<WishlistResponse>> getMyWishList() {
        return ResponseEntity.ok(wishListService.getMyWishList());
    }

    @DeleteMapping("/{bookId}")
    public ResponseEntity<Void> removeFromWishList(@PathVariable Long bookId) {
        wishListService.removeFromWishList(bookId);
        return ResponseEntity.noContent().build();
    }
}


