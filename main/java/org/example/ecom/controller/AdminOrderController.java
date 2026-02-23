package org.example.ecom.controller;

import jakarta.validation.Valid;
import org.example.ecom.service.OrderService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/orders")
public class AdminOrderController {

    private final OrderService orderService;

    public AdminOrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PutMapping("/{id}/confirm")
    public void confirm(@PathVariable Long id) {
        orderService.confirmOrder(id);
    }

    @PutMapping("/{id}/ship")
    public void ship(@PathVariable Long id) {
        orderService.shipOrder(id);
    }

    @PutMapping("/{id}/deliver")
    public void deliver(@PathVariable Long id) {
        orderService.deliverOrder(id);
    }
}

