package org.example.ecom.controller;

import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
import org.example.ecom.entity.Payment;
import org.example.ecom.repository.PaymentRepository;
import org.example.ecom.service.OrderService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.example.ecom.entity.PaymentStatus;

@RestController
@RequestMapping("/webhooks/stripe")
public class StripeWebhookController {
    @Value("${stripe.webhook-secret}")
    private String webhookSecret;

    private final PaymentRepository paymentRepository;
    private final OrderService orderService;

    public StripeWebhookController(PaymentRepository paymentRepository,OrderService orderService){
        this.paymentRepository =paymentRepository;
        this.orderService = orderService;
    }
    @PostMapping
    public ResponseEntity<String> handleWebhook (@RequestBody String payload, @RequestHeader("Stripe-Signature") String sigHeader) throws Exception{
        Event event = Webhook.constructEvent(payload,sigHeader,webhookSecret);
        if ("payment_intent.succeeded".equals(event.getType())) {

            PaymentIntent intent =
                    (PaymentIntent) event.getDataObjectDeserializer()
                            .getObject()
                            .orElseThrow();

            Payment payment = paymentRepository
                    .findByStripePaymentIntentId(intent.getId())
                    .orElseThrow();

            payment.setStatus(PaymentStatus.SUCCESS);
            paymentRepository.save(payment);

            orderService.confirmOrder(payment.getOrder().getOrderId());
        }
        return ResponseEntity.ok("ok");
    }

}
