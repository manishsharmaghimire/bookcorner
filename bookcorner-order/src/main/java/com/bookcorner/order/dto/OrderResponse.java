package com.bookcorner.order.dto;

import com.bookcorner.order.enums.OrderStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponse {


    private Long orderID;
    private String orderNumber;
    private OrderStatus status;
    private BigDecimal totalAmount;
    private Instant orderedAt;
    private List<OrderItemResponse> orderItems;



}
