package org.example.ecom.service;

import org.example.ecom.dtos.response.CartItemResponse;
import org.example.ecom.dtos.response.CartResponse;
import org.example.ecom.entity.Cart;
import org.example.ecom.entity.CartItem;
import org.example.ecom.entity.User;
import org.example.ecom.entity.Product;
import org.example.ecom.repository.CartItemRepository;
import org.example.ecom.repository.CartRepository;
import org.example.ecom.repository.ProductRepository;
import org.example.ecom.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public CartService(
            CartRepository cartRepository,
            CartItemRepository cartItemRepository,
            UserRepository userRepository,
            ProductRepository productRepository) {

        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }
    public void addToCart(Long productId,Integer quantity, String userEmail){
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(()->new RuntimeException("No user is found with this email"));

        Cart cart = cartRepository.findByUser(user)
                .orElseGet(()->{
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });

        Product product = productRepository.findById(productId)
                .orElseThrow(()-> new RuntimeException("product not found"));
        CartItem item = cartItemRepository
                .findByCartIdAndProductId(cart.getId(), productId)
                .orElse(null);

        if (item == null) {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(quantity);
            cart.getItems().add(newItem);
        } else {
            item.setQuantity(item.getQuantity() + quantity);
        }

        cartRepository.save(cart);
    }
    public CartResponse viewCart(String userEmail){

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(()->new RuntimeException("User Email not found"));

        Cart cart = cartRepository.findByUser(user)
                .orElse(null);
        CartResponse response = new CartResponse();

        if (cart == null || cart.getItems().isEmpty()) {
            response.setItems(List.of());
            response.setCartTotal(BigDecimal.ZERO);
            return response;
        }

        List<CartItemResponse> items = new ArrayList<>();
        BigDecimal cartTotal = BigDecimal.ZERO;

        for (CartItem item : cart.getItems()) {

            CartItemResponse itemResponse = new CartItemResponse();
            itemResponse.setProductId(item.getProduct().getId());
            itemResponse.setProductName(item.getProduct().getName());
            itemResponse.setPrice(item.getProduct().getPrice());
            itemResponse.setQuantity(item.getQuantity());

            BigDecimal itemTotal = item.getProduct().getPrice()
                    .multiply(BigDecimal.valueOf(item.getQuantity()));

            itemResponse.setItemTotal(itemTotal);

            cartTotal = cartTotal.add(itemTotal);
            items.add(itemResponse);
        }
        response.setItems(items);
        response.setCartTotal(cartTotal);

        return response;
    }
    //We don't save the totals directly in DB it is implemented
    public void updateQuantity(String userEmail,Long productId, Integer quantity){
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(()-> new RuntimeException("User not found"));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(()-> new RuntimeException("Cart not found"));

        CartItem cartItem = cartItemRepository.findByCartIdAndProductId(cart.getId(), productId)  //makes sure there are no cross updates
                .orElseThrow(()-> new RuntimeException("Product not found"));

        if(quantity==0){  //remove item when the quantity is 0
            cart.getItems().remove(cartItem);
            cartItemRepository.delete(cartItem);
            return;
        }
        cartItem.setQuantity(quantity);
        cartItemRepository.save(cartItem);

    }
    public void removeItem(String userEmail, Long productId){
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(()-> new RuntimeException("User not found"));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(()-> new RuntimeException("Cart not found"));

        CartItem cartItem = cartItemRepository.findByCartIdAndProductId(cart.getId(), productId)  //makes sure there are no cross updates
                .orElseThrow(()-> new RuntimeException("Product not found"));

        cart.getItems().remove(cartItem);
        cartItemRepository.delete(cartItem);
    }
    }

