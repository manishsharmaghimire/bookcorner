package com.bookcorner.cart.mapper;

import com.bookcorner.cart.dto.CartItemResponse;
import com.bookcorner.cart.dto.CartResponse;
import com.bookcorner.cart.entity.Cart;
import com.bookcorner.cart.entity.CartItem;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class CartMapper {


    public CartItemResponse toCartItemResponse(CartItem cartItem) {

        BigDecimal subTotal =
                cartItem.getBook()
                        .getPrice()
                        .multiply(BigDecimal.valueOf(cartItem.getQuantity()));

        return CartItemResponse.builder()
                .cartItemId(cartItem.getId())
                .bookId(cartItem.getBook().getId())
                .title(cartItem.getBook().getTitle())
                .authorName(cartItem.getBook().getAuthor().getAuthorName())
                .coverImageUrl(cartItem.getBook().getCoverImageUrl())
                .price(cartItem.getBook().getPrice())
                .quantity(cartItem.getQuantity())
                .subTotal(subTotal)
                .build();


    }

    public CartResponse toCartResponse(Cart cart) {

        List<CartItemResponse> items = cart.getCartItems()
                .stream()
                .map(this::toCartItemResponse)
                .toList();

        BigDecimal grandTotal = items.stream()
                .map(CartItemResponse::getSubTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return CartResponse.builder()
                .cartId(cart.getId())
                .items(items)
                .grandTotal(grandTotal)
                .build();
    }

}
