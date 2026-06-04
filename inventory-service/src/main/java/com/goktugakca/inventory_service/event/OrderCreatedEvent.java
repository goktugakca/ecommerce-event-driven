package com.goktugakca.inventory_service.event;

public record OrderCreatedEvent(
        Long orderId,
        String productSku,
        int quantity
) {
}