package org.example.ecom.service;

import jakarta.transaction.Transactional;
import org.example.ecom.entity.Order;
import org.example.ecom.entity.OrderStatus;
import org.example.ecom.repository.OrderRepository;
import org.example.ecom.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    public OrderService(UserRepository userRepository, OrderRepository orderRepository){
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public void confirmOrder(Long orderId) {
        Order order = getOrder(orderId);
        ensureStatus(order, OrderStatus.PENDING);
        order.setOrderStatus(OrderStatus.CONFIRMED);
        orderRepository.save(order);
    }

    @Transactional
    public void shipOrder(Long orderId) {
        Order order = getOrder(orderId);
        ensureStatus(order, OrderStatus.CONFIRMED);
        order.setOrderStatus(OrderStatus.SHIPPED);
        orderRepository.save(order);
    }
    @Transactional
    public void deliverOrder(Long orderId) {
        Order order = getOrder(orderId);
        ensureStatus(order, OrderStatus.SHIPPED);
        order.setOrderStatus(OrderStatus.DELIVERED);
        orderRepository.save(order);
    }

    @Transactional
    public void cancelOrder(Long orderId) {
        Order order = getOrder(orderId);

        if (order.getOrderStatus() == OrderStatus.DELIVERED) {
            throw new RuntimeException("Delivered orders cannot be cancelled");
        }

        order.setOrderStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
    }
    private Order getOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    private void ensureStatus(Order order, OrderStatus expected) {
        if (order.getOrderStatus() != expected) {
            throw new RuntimeException(
                    "Invalid order status. Expected " + expected +
                            " but was " + order.getOrderStatus()
            );

        }
}}
