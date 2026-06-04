package com.goktugakca.order_service.dto;

public record CreateOrderRequest(
        String productSku,
        int quantity
) {
}