package com.bookcorner.payment.repository;

import com.bookcorner.order.entity.OrderEntity;
import com.bookcorner.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByTransactionId(String transactionId);
    Optional<Payment> findByOrder(OrderEntity order);
}
