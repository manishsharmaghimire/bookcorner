package com.bookcorner.payment.repository;

import com.bookcorner.order.entity.OrderEntity;
import com.bookcorner.payment.entity.Payment;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByMerchantTransactionId(String merchantTransactionId);
    Optional<Payment> findByOrder(OrderEntity order);
    Optional<Payment> findByGatewayTransactionId(String gatewayTransactionId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Payment p WHERE p.order = :order")
    Optional<Payment> findByOrderForUpdate(@Param("order") OrderEntity order);
}
