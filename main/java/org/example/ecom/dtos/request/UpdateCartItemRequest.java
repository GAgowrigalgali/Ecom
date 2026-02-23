package org.example.ecom.dtos.request;
//To have the automatic arithmatic update of quantity when the same userId and same productId is added
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class UpdateCartItemRequest {

    @NotNull
    private Long productId;

    @Min(0)   //Once the item quantity is 0 it should be removed from cart
    private Integer quantity;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
