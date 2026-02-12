package org.example.ecom.controller;


import org.example.ecom.dtos.response.PaymentResponse;
import org.example.ecom.service.PaymentService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments/stripe")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/create-intent/{orderId}")
    public PaymentResponse createIntent(@PathVariable Long orderId) throws Exception{
        return paymentService.createPaymentIntent(orderId);
    }
}

