package com.bookcorner.payment.service.serviceimpl;

import com.bookcorner.auth.security.AuthenticationService;
import com.bookcorner.order.entity.OrderEntity;
import com.bookcorner.order.repository.OrderRepository;
import com.bookcorner.payment.dto.PaymentRequest;
import com.bookcorner.payment.dto.PaymentResponse;
import com.bookcorner.payment.entity.Payment;
import com.bookcorner.payment.enums.PaymentStatus;
import com.bookcorner.payment.exception.PaymentNotFoundException;
import com.bookcorner.payment.mapper.PaymentMapper;
import com.bookcorner.payment.repository.PaymentRepository;
import com.bookcorner.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        var orderEntity = orderRepository.findByOrderNumberAndUser(user, request.getOrderNumber()).orElseThrow(() ->
                new RuntimeException("Order not found.")
        );


         paymentRepository.findByOrder(orderEntity).ifPresent(order -> {new RuntimeException("Payment already exists for order: " + orderEntity.getOrderNumber())});


         Payment payment = new Payment();
         payment.setPaymentMethod(request.getPaymentMethod());
         payment.setPaymentStatus(PaymentStatus.PENDING);
         payment.setAmount(orderEntity.getTotalAmount());
        Payment savedPayment = paymentRepository.save(payment);
        return  paymentMapper.toPaymentResponse(savedPayment);





    }

    @Override
    public PaymentResponse getPaymentById(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new PaymentNotFoundException("Payment not found with id: " + id));
        return paymentMapper.toDto(payment);
    }

    @Override
    public PaymentResponse getPaymentByOrderId(Long orderId) {
        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new PaymentNotFoundException("Payment not found for order id: " + orderId));
        return paymentMapper.toDto(payment);
    }

    @Override
    public List<PaymentResponse> getMyPayments() {
        var user = authenticationService.getAuthenticatedUser();
        return paymentRepository.findByUserId(user.getId()).stream()
                .map(paymentMapper::toDto)
                .toList();
    }
}
