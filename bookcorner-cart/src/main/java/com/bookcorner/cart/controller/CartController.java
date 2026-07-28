package com.bookcorner.cart.controller;

import com.bookcorner.cart.dto.AddQuantityRequest;
import com.bookcorner.cart.dto.CartResponse;
import com.bookcorner.cart.dto.UpdateCartItemRequest;
import com.bookcorner.cart.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/add/{bookId}")
    public ResponseEntity<CartResponse> addToCart(
            @PathVariable Long bookId,
            @Valid @RequestBody AddQuantityRequest request) {
        return ResponseEntity.ok(cartService.addToCart(bookId, request));
    }

    @GetMapping
    public ResponseEntity<CartResponse> getCart() {
        return ResponseEntity.ok(cartService.getCart());
    }

    @PutMapping("/items/{cartItemId}")
    public ResponseEntity<CartResponse> updateCartItem(
            @PathVariable Long cartItemId,
            @Valid @RequestBody UpdateCartItemRequest request) {
        return ResponseEntity.ok(cartService.updateCart(cartItemId, request));
    }

    @DeleteMapping("/items/{cartItemId}")
    public ResponseEntity<CartResponse> removeItem(@PathVariable Long cartItemId) {
        return ResponseEntity.ok(cartService.renoveItem(cartItemId));
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearCart() {
        cartService.clearCart();
        return ResponseEntity.noContent().build();
    }
}
