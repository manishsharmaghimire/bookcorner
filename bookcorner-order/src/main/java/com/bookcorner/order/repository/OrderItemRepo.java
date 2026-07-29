package com.bookcorner.order.repository;

import com.bookcorner.order.entity.OrderEntity;
import com.bookcorner.order.entity.OrderItem;
import com.bookcorner.order.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepo extends JpaRepository<OrderItem,Long> {


    List<OrderItem> findByOrder(OrderEntity order);
}
