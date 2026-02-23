package org.example.ecom.dtos.request;

import jakarta.validation.constraints.NotNull;

public class PaymentRequest {

    @NotNull
    private Long orderId;

    @NotNull
    private String paymentMethod; // CARD / UPI

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
