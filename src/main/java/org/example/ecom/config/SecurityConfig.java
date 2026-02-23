package org.example.ecom.config;
//without this file spring security secures all the endpoints and every endpoint would give a 401 non authorised error
import org.example.ecom.security.JwtAuthFilter;
import org.example.ecom.security.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration //executes the spring bean methods after creating the class at startup
@EnableWebSecurity //turns on spring security for this application --> security filter chain
    public class SecurityConfig {

        private final JwtUtil jwtUtil;

        public SecurityConfig(JwtUtil jwtUtil) {
            this.jwtUtil = jwtUtil;
        }

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //security filtering chain bean --> without this all endpoints are locked and JWT will be ignored
            http
                    .csrf(csrf -> csrf.disable()) //cookie based sessions are disabled, don't work well with JWT as it is sent in headers
                    .sessionManagement(
                            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS) //makes sure no sessions are created and data is stored, pure stateless behavoir
                    )
                    .authorizeHttpRequests(auth -> auth
                            .requestMatchers("/auth/**").permitAll()   //anybody can access login and auth
                            .requestMatchers("/admin/**").hasRole("ADMIN") //only Admin role can access these endpoints
                            .anyRequest().authenticated() //every other end point requires a logged in user
                    )
                    .addFilterBefore(
                            new JwtAuthFilter(jwtUtil),
                            UsernamePasswordAuthenticationFilter.class
                    ); //run the JWT filter before Spring's auth filter as JWT restores authentication

            return http.build(); //fixes these configurations
        }
    }


