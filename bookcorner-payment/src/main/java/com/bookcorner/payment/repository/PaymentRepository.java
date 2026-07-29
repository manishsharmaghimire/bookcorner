package com.bookcorner.payment.repository;

import com.bookcorner.order.entity.OrderEntity;
import com.bookcorner.order.entity.OrderItem;
import com.bookcorner.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByUserId(Long userId);
    Optional<Payment> findByOrderId(Long orderId);
    Optional<Payment> findByTransactionId(String transactionId);
    Optional<Payment> findByOrder(OrderEntity order);
}
