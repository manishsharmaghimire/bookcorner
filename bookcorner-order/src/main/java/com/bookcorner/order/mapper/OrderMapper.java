package com.bookcorner.order.mapper;

import com.bookcorner.order.dto.OrderItemResponse;
import com.bookcorner.order.dto.OrderResponse;
import com.bookcorner.order.entity.OrderEntity;
import com.bookcorner.order.entity.OrderItem;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderMapper {


    public OrderItemResponse toOrderItemResponse(OrderItem orderItem) {


     return    OrderItemResponse.builder()
                .bookId(orderItem.getBook().getId())
                .title(orderItem.getBook().getTitle())
                .authorName(orderItem.getBook().getAuthor().getAuthorName())
                .coverImageUrl(orderItem.getBook().getCoverImageUrl())
                .price(orderItem.getPrice())
                .quantity(orderItem.getQuantity())
                .subTotal(orderItem.getSubTotal())
                .build();



    }
    public OrderResponse toOrderResponse(OrderEntity order) {

        List<OrderItemResponse> items = order.getOrderItems()
                .stream()
                .map(this::toOrderItemResponse)
                .toList();

        return OrderResponse.builder()
                .orderID(order.getId())
                .orderNumber(order.getOrderNumber())
                .status(order.getStatus())
                .totalAmount(order.getTotalAmount())
                .orderedAt(order.getCreatedAt())
                .orderItems(items)
                .build();
    }



}
