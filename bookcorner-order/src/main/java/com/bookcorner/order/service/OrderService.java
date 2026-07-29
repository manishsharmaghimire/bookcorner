package com.bookcorner.order.service;


import com.bookcorner.auth.entity.User;
import com.bookcorner.auth.security.AuthenticationService;
import com.bookcorner.books.entity.Books;
import com.bookcorner.cart.entity.Cart;
import com.bookcorner.cart.entity.CartItem;
import com.bookcorner.cart.repository.CartRepo;
import com.bookcorner.order.dto.OrderResponse;
import com.bookcorner.order.entity.OrderEntity;
import com.bookcorner.order.entity.OrderItem;
import com.bookcorner.order.enums.OrderStatus;
import com.bookcorner.order.mapper.OrderMapper;
import com.bookcorner.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final AuthenticationService authenticationService;
    private final OrderRepository orderRepository;
    private  final CartRepo cartRepository;
    private final OrderMapper orderMapper;

   public OrderResponse placeOrder() {


       User authenticatedUser = authenticationService.getAuthenticatedUser();
       // 2. Find user's cart
       Cart cart = cartRepository.findByUser(authenticatedUser)
               .orElseThrow(() ->
                       new RuntimeException("Cart not found.")
               );


       if (cart.getCartItems().isEmpty()) {

           throw new RuntimeException("Cart is empty.");
       }

       OrderEntity order = OrderEntity.builder()
               .user(authenticatedUser)
               .orderNumber(generateOrderNumber())
               .totalAmount(BigDecimal.ZERO)
               .status(OrderStatus.PENDING)
               .orderItems(new ArrayList<>())
               .build();

       BigDecimal amount = BigDecimal.ZERO;

       for (CartItem cartItem : cart.getCartItems()) {
           Books book = cartItem.getBook();


           // 1. Check stock
           if (book.getStock() < cartItem.getQuantity()) {
               throw new RuntimeException(
                       book.getTitle() + " has insufficient stock."
               );


           }
           var price = book.getPrice();
           BigDecimal subTotal = price.multiply(BigDecimal.valueOf(cartItem.getQuantity()));


           OrderItem orderItem = OrderItem.builder()
                   .order(order)
                   .book(book)
                   .quantity(cartItem.getQuantity())
                   .price(price)
                   .subTotal(subTotal)
                   .build();

           order.getOrderItems().add(orderItem);

           amount = amount.add(subTotal);
       }
       order.setTotalAmount(amount);

       // 7. Save order
       OrderEntity savedOrder = orderRepository.save(order);
       cart.getCartItems().clear();
       return orderMapper.toOrderResponse(savedOrder);

   }

   public OrderResponse getOrderById(String orderId) {

       var authenticatedUser = authenticationService.getAuthenticatedUser();

       var order = orderRepository.findByOrderNumber(orderId)
               .orElseThrow(() -> new RuntimeException("Order not found."));

       if(!order.getUser().getId().equals(authenticatedUser.getId())) {
           throw new RuntimeException("Access denied.");

       }
       return orderMapper.toOrderResponse(order);


   }

   public List<OrderResponse> getMyOrders(){

       var authenticatedUser = authenticationService.getAuthenticatedUser();

       var orders = orderRepository.findByUserOrderByCreatedAtDesc(authenticatedUser);

       return orders.stream().map(orderMapper::toOrderResponse).toList();

   }




    private String generateOrderNumber() {
        return "ORD-" + System.currentTimeMillis();
    }

}
