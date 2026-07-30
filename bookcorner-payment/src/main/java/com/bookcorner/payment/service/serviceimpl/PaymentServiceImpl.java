package com.bookcorner.payment.service.serviceimpl;

import com.bookcorner.auth.security.AuthenticationService;
import com.bookcorner.order.entity.OrderEntity;
import com.bookcorner.order.enums.OrderStatus;
import com.bookcorner.order.exception.OrderNotFoundException;
import com.bookcorner.order.repository.OrderRepository;
import com.bookcorner.payment.dto.PaymentRequest;
import com.bookcorner.payment.dto.PaymentResponse;
import com.bookcorner.payment.entity.Payment;
import com.bookcorner.payment.enums.PaymentStatus;
import com.bookcorner.payment.exception.PaymentAlreadyExistsException;
import com.bookcorner.payment.exception.PaymentNotFoundException;
import com.bookcorner.payment.mapper.PaymentMapper;
import com.bookcorner.payment.repository.PaymentRepository;
import com.bookcorner.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final AuthenticationService authenticationService;
    private final OrderRepository orderRepository;

    @Override
    @Transactional
    public PaymentResponse processPayment(PaymentRequest request) {

        var user = authenticationService.getAuthenticatedUser();

        OrderEntity order = orderRepository
                .findByOrderNumberAndUser(request.getOrderNumber(), user)
                .orElseThrow(() ->
                        new OrderNotFoundException(
                                "Order not found: " + request.getOrderNumber()
                        )
                );

        paymentRepository.findByOrder(order)
                .ifPresent(existing -> {
                    throw new PaymentAlreadyExistsException(
                            "Payment already exists for order: "
                                    + order.getOrderNumber()
                    );
                });

        Payment payment = Payment.builder()
                .order(order)
                .paymentMethod(request.getPaymentMethod())
                .paymentStatus(PaymentStatus.PENDING)
                .amount(order.getTotalAmount())
                .build();

        Payment savedPayment = paymentRepository.save(payment);

        return paymentMapper.toPaymentResponse(savedPayment);
    }



    @Override
    @Transactional
    public PaymentResponse markPaymentSuccess(String transactionId) {

        Payment payment = paymentRepository
                .findByTransactionId(transactionId)
                .orElseThrow(() ->
                        new PaymentNotFoundException(
                                "Payment not found with transaction id: "
                                        + transactionId
                        )
                );

        payment.setPaymentStatus(PaymentStatus.COMPLETED);

        payment.getOrder().setStatus(OrderStatus.CONFIRMED);
        orderRepository.save(payment.getOrder());

        Payment savedPayment = paymentRepository.save(payment);

        return paymentMapper.toPaymentResponse(savedPayment);
    }

    @Override
    @Transactional
    public PaymentResponse markPaymentFailed(String transactionId) {

        Payment payment = paymentRepository
                .findByTransactionId(transactionId)
                .orElseThrow(() ->
                        new PaymentNotFoundException(
                                "Payment not found with transaction id: "
                                        + transactionId
                        )
                );

        payment.setPaymentStatus(PaymentStatus.FAILED);

        Payment savedPayment = paymentRepository.save(payment);

        return paymentMapper.toPaymentResponse(savedPayment);
    }
    @Override
    public PaymentResponse getPaymentByOrder(String orderNumber) {

        var user = authenticationService.getAuthenticatedUser();

        OrderEntity order = orderRepository
                .findByOrderNumberAndUser(orderNumber, user)
                .orElseThrow(() ->
                        new OrderNotFoundException(
                                "Order not found: " + orderNumber
                        )
                );

        Payment payment = paymentRepository.findByOrder(order)
                .orElseThrow(() ->
                        new PaymentNotFoundException(
                                "Payment not found for order: " + orderNumber
                        )
                );

        return paymentMapper.toPaymentResponse(payment);
    }
}