package com.webapp.ecom.dto;

import com.webapp.ecom.enums.OrderStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class OrderDto {
    private Long id;
    private Long userId;
    private LocalDate orderDate;
    private BigDecimal totalAmount;
    private OrderStatus orderStatus;
    private List<OrderItemDto> items;
}
