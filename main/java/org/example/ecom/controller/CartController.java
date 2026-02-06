package org.example.ecom.controller;

import org.example.ecom.dtos.request.UpdateCartItemRequest;
import org.example.ecom.dtos.response.CartResponse;
import org.example.ecom.service.CartService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService){
        this.cartService = cartService;
    }
    @PostMapping("/add")
    public void addToCart(@RequestParam Long productId, @RequestParam Integer quantity){
        String userEmail = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        cartService.addToCart(productId, quantity, userEmail);
    }
    @GetMapping
    public CartResponse viewCart() {

        String userEmail = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return cartService.viewCart(userEmail);
    }
    @PutMapping("/update")
    public void updateCart( @RequestBody UpdateCartItemRequest updateCartItemRequest){

        String userEmail = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
         cartService.updateQuantity(userEmail,updateCartItemRequest.getProductId(),updateCartItemRequest.getQuantity());
    }
    @DeleteMapping("/items/{productId}")

    public void removeCart( @PathVariable Long productId){

        String userEmail = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
        cartService.removeItem(userEmail, productId);

    }
}




