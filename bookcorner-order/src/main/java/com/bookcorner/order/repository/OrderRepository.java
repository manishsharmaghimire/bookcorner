package com.bookcorner.order.repository;

import com.bookcorner.auth.entity.User;
import com.bookcorner.order.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface Order extends JpaRepository<OrderEntity, Long> {



    List<Order> findByUserOrderByCreatedAtDesc(User user);
    OrderEntity findByOrderNumber(String orderNumber);
}
