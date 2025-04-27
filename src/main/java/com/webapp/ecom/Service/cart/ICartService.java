package com.webapp.ecom.Service.cart;

import com.webapp.ecom.model.Cart;
import com.webapp.ecom.model.User;

import java.math.BigDecimal;

public interface ICartService {
    Cart getCart(Long Id);
    void clearCart(Long Id);
    BigDecimal getTotalPrice(Long Id);

    Cart initializeCart(User user);

    Cart getCartByUserId(Long userId);

}
