package org.example.ecom.service;

import jakarta.transaction.Transactional;
import org.example.ecom.dtos.response.OrderItemResponse;
import org.example.ecom.dtos.response.OrderResponse;
import org.example.ecom.entity.*;
import org.example.ecom.repository.CartRepository;
import org.example.ecom.repository.OrderRepository;
import org.example.ecom.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CheckoutService {
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;

    public CheckoutService(UserRepository userRepository,OrderRepository orderRepository,CartRepository cartRepository){
        this.cartRepository=cartRepository;
        this.orderRepository =orderRepository;
        this.userRepository = userRepository;
    }
    @Transactional   //makes sure there is a rollback, if any steps fail
    public OrderResponse checkout(String userEmail){
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(()-> new RuntimeException("User not found"));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(()-> new RuntimeException("Cart not found"));

        if(cart.getItems().isEmpty()){
            throw new RuntimeException("cart is empty");
        }

        Order order = new Order();
        order.setUser(user);
        order.setOrderStatus(OrderStatus.PENDING);

        BigDecimal total = BigDecimal.ZERO;

        for (CartItem ci : cart.getItems()) {
            OrderItem oi = new OrderItem();
            oi.setOrder(order);
            oi.setProduct(ci.getProduct());
            oi.setQuantity(ci.getQuantity());
            oi.setPrice(ci.getProduct().getPrice());

            BigDecimal itemTotal =
                    ci.getProduct().getPrice()
                            .multiply(BigDecimal.valueOf(ci.getQuantity()));

            total = total.add(itemTotal);
            order.getOrderItems().add(oi);
        }
        order.setTotalAmount(total);

        Order saved = orderRepository.save(order);

        // clear cart
        cart.getItems().clear();
        cartRepository.save(cart);

        return mapToResponse(saved);

    }
    private OrderResponse mapToResponse(Order order) {
        OrderResponse res = new OrderResponse();
        res.setOrderId(order.getOrderId());
        res.setStatus(order.getOrderStatus().name());
        res.setTotalAmount(order.getTotalAmount());

        List<OrderItemResponse> items = order.getOrderItems().stream().map(oi -> {
            OrderItemResponse r = new OrderItemResponse();
            r.setProductId(oi.getProduct().getId());
            r.setProductName(oi.getProduct().getName());
            r.setQuantity(oi.getQuantity());
            r.setPrice(oi.getPrice());
            return r;
        }).toList();

        res.setItems(items);
        return res;}}

