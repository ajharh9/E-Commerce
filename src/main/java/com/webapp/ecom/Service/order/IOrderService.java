package com.webapp.ecom.Service.order;

import com.webapp.ecom.dto.OrderDto;
import com.webapp.ecom.model.Order;

import java.util.List;

public interface IOrderService {
    Order placeOrder(Long userId);
    OrderDto getOrder(Long orderId);

    List<OrderDto> getUserOrders(Long userId);
}
