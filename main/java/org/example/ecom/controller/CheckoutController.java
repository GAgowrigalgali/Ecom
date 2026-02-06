package org.example.ecom.controller;

import org.example.ecom.dtos.response.OrderResponse;

import org.example.ecom.service.CheckoutService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/checkout")
public class CheckoutController {

    private final CheckoutService checkoutService;

    public CheckoutController(CheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }

    @PostMapping
    public OrderResponse checkout() {
        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return checkoutService.checkout(email);
    }
}

