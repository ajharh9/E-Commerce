package com.webapp.ecom.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal totalAmount= BigDecimal.ZERO;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CartItem> cartItems = new HashSet<>();

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public void addItem(CartItem item){
        this.cartItems.add(item);
        item.setCart(this);
        updateAmount();
    }

    public void removeItem(CartItem item){
        this.cartItems.remove(item);
        item.setCart(null);
        updateAmount();
    }

    public void updateAmount() {
        this.totalAmount = cartItems.stream()
                .map((item)->{
                        BigDecimal uniPrice = item.getUnitPrice();
                        return uniPrice==null?BigDecimal.ZERO:
                                item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
                })
              .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public String toString() {
        return "Cart{" +
                "id=" + id +
                ", totalAmount=" + totalAmount +
                ", cartItems=" + cartItems +
                '}';
    }
}
