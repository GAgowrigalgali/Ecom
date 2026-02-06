package org.example.ecom.dtos.response;

import java.math.BigDecimal;
import java.util.List;
//Whole cart summary
public class CartResponse {

    private List<CartItemResponse> items;
    private BigDecimal cartTotal;

    public List<CartItemResponse> getItems() {
        return items;
    }

    public void setItems(List<CartItemResponse> items) {
        this.items = items;
    }

    public BigDecimal getCartTotal() {
        return cartTotal;
    }

    public void setCartTotal(BigDecimal cartTotal) {
        this.cartTotal = cartTotal;
    }
}
