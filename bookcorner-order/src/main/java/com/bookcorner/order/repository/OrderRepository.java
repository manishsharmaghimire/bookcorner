package com.bookcorner.order.repository;

import com.bookcorner.auth.entity.User;
import com.bookcorner.order.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {



    List<OrderEntity> findByUserOrderByCreatedAtDesc(User user);
    Optional<OrderEntity> findByOrderNumber(String orderNumber);

    Optional<OrderEntity> findByOrderNumberAndUser(String orderNumber, User user);
}
