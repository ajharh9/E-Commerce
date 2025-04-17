package com.webapp.ecom.repository;

import com.webapp.ecom.dto.OrderDto;
import com.webapp.ecom.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Long> {
    List<Order> findByUserId(Long userId);


}
