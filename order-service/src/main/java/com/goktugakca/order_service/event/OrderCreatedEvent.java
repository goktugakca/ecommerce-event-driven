package com.goktugakca.order_service.event;

public record OrderCreatedEvent(
        Long orderId,
        String productSku,
        int quantity
) {
}