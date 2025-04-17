package com.webapp.ecom.Service.cart;

import com.webapp.ecom.model.Cart;

import java.math.BigDecimal;

public interface ICartService {
    Cart getCart(Long Id);
    void clearCart(Long Id);
    BigDecimal getTotalPrice(Long Id);
    Long initializeCart();
    Cart getCartByUserId(Long userId);

}
