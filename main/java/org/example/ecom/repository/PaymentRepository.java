package org.example.ecom.repository;

import org.example.ecom.entity.Order;
import org.example.ecom.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment,Long> {
    boolean existsByOrder(Order order);
    Optional<Payment> findByStripePaymentIntentId(String stripePaymentIntentId);
}
