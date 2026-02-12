package org.example.ecom.config;

import com.stripe.Stripe;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

//This initializes Stripe once at startup.

@Configuration
public class StripeConfig {
    @Value("${stripe.secret-key}")
    private String secretKey;

    @PostConstruct  //must be executed immediately after creation of a bean and its dependency injection before it is put into service
    public void init() {
        Stripe.apiKey = secretKey;
    }

}
