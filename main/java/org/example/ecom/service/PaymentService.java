package org.example.ecom.service;

import com.stripe.model.PaymentIntent;
import jakarta.transaction.Transactional;

import org.example.ecom.dtos.response.PaymentResponse;
import org.example.ecom.entity.*;
import org.example.ecom.repository.OrderRepository;
import org.example.ecom.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class PaymentService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;


    public PaymentService(OrderRepository orderRepository,
                          PaymentRepository paymentRepository) {
        this.orderRepository = orderRepository;
        this.paymentRepository = paymentRepository;
    }

   @Transactional
        public PaymentResponse createPaymentIntent(Long orderId) throws Exception {

            Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (order.getOrderStatus() != OrderStatus.PENDING) {
            throw new RuntimeException("Order not payable");
        }

        if (paymentRepository.existsByOrder(order)) {
            throw new RuntimeException("Payment already attempted");
        }

        //STRIPE integration
       Map<String,Object> params = new HashMap<>();
        params.put("amount",order.getTotalAmount().multiply(BigDecimal.valueOf(100)).longValue());
        params.put("currency","INR");
       params.put("automatic_payment_methods", Map.of("enabled", true));

       PaymentIntent intent = PaymentIntent.create(params);

       Payment payment = new Payment();
       payment.setOrder(order);
       payment.setAmount(order.getTotalAmount());
       payment.setStatus(PaymentStatus.INITIATED);
       payment.setMethod(PaymentMethod.CARD);
       payment.setStripePaymentTransactionId(intent.getId());

       paymentRepository.save(payment);

       PaymentResponse response = new PaymentResponse();
       response.setPaymentId(payment.getId());
       response.setStatus(payment.getStatus().name());
       response.setClientId(intent.getClientSecret());

       return response;

    }

}
