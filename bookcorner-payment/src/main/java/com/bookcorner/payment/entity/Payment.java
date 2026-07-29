package com.bookcorner.payment.entity;

import com.bookcorner.order.entity.OrderEntity;
import com.bookcorner.payment.enums.PaymentMethod;
import com.bookcorner.payment.enums.PaymentStatus;
import com.bookcorner.shared.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment extends BaseEntity {



    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id",unique = true, nullable = false)
    private OrderEntity order;


    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false)
    private  PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false)
    private  PaymentStatus paymentStatus;
    @Column(nullable = false, precision = 10, scale = 2)
    private String transactionId;
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

}
