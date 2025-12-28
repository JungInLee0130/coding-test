package com.seowon.coding.domain.dto;

import com.seowon.coding.domain.model.Order;
import com.seowon.coding.domain.model.OrderItem;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class OrderResponse {
    private String customerName;

    private String customerEmail;
    private Order.OrderStatus status;

    private LocalDateTime orderDate;

    private List<OrderItem> items = new ArrayList<>();

    private BigDecimal totalAmount;

    @Builder
    public OrderResponse(String customerName, String customerEmail, Order.OrderStatus status, LocalDateTime orderDate, List<OrderItem> items, BigDecimal totalAmount) {
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.status = status;
        this.orderDate = orderDate;
        this.items = items;
        this.totalAmount = totalAmount;
    }
}
