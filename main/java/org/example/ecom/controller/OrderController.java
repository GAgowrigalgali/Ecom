package org.example.ecom.controller;

import org.example.ecom.entity.Order;
import org.example.ecom.entity.User;
import org.example.ecom.repository.OrderRepository;
import org.example.ecom.repository.UserRepository;
import org.example.ecom.service.OrderService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    public OrderController(OrderService orderService,
                           UserRepository userRepository,
                           OrderRepository orderRepository) {
        this.orderService = orderService;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
    }

    @PutMapping("/{id}/cancel")
    public void cancel(@PathVariable Long id) {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getUser().getUserId().equals(user.getUserId())) {
            throw new RuntimeException("Cannot cancel others' orders");
        }

        orderService.cancelOrder(id);
    }
}

