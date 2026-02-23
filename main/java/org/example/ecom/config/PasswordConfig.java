package org.example.ecom.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//passwords are one way hashed, every time for the same password it will be a different hash,hence cannot be reversed

    @Configuration  //allows defining beans that are dependent on other beans ensuring initialising
    public class PasswordConfig {

        @Bean
        public BCryptPasswordEncoder passwordEncoder(){
            return new BCryptPasswordEncoder();
        }

}
