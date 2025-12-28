package com.seowon.coding.domain.model;

import com.seowon.coding.service.OrderProduct;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders") // "order" is a reserved keyword in SQL
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String customerName;
    
    private String customerEmail;
    
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    
    private LocalDateTime orderDate;
    
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<OrderItem> items = new ArrayList<>();
    
    private BigDecimal totalAmount;


    
    // Business logic
    public void addItem(OrderItem item) {
        items.add(item);
        item.setOrder(this);
        recalculateTotalAmount();
    }
    
    public void removeItem(OrderItem item) {
        items.remove(item);
        item.setOrder(null);
        recalculateTotalAmount();
    }
    
    public void recalculateTotalAmount() {
        this.totalAmount = items.stream()
                .map(OrderItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    public void markAsProcessing() {
        this.status = OrderStatus.PROCESSING;
    }
    
    public void markAsShipped() {
        this.status = OrderStatus.SHIPPED;
    }
    
    public void markAsDelivered() {
        this.status = OrderStatus.DELIVERED;
    }
    
    public void markAsCancelled() {
        this.status = OrderStatus.CANCELLED;
    }
    
    public enum OrderStatus {
        PENDING, PROCESSING, SHIPPED, DELIVERED, CANCELLED
    }

    public static Order createOrder(String customerName, String customerEmail) {
        return Order.builder()
                .customerName(customerName)
                .customerEmail(customerEmail)
                .status(Order.OrderStatus.PENDING)
                .orderDate(LocalDateTime.now())
                .items(new ArrayList<>())
                .totalAmount(BigDecimal.ZERO)
                .build();
    }

    public static void validateCustomerInfo(String customerName, String customerEmail) {
        if (customerName == null || customerEmail == null) {
            throw new IllegalArgumentException("customer info required");
        }
    }


    public void validateQuantity(Integer inputQuantity, Integer stockQuantity, Long productId) {
        if (inputQuantity <= 0) {
            throw new IllegalArgumentException("quantity must be positive: " + inputQuantity);
        }
        if (stockQuantity < inputQuantity) {
            throw new IllegalStateException("insufficient stock for product " + productId);
        }
    }

    public BigDecimal increaseSubtotal(BigDecimal subTotal) {
        return this.totalAmount.add(subTotal);
    }


    public void setTotalAmount(BigDecimal subtotal,
                               BigDecimal shipping,
                               BigDecimal discount) {
        this.totalAmount.add(subtotal).add(shipping).subtract(discount);
    }
}