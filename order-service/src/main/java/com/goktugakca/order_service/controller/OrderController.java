package com.goktugakca.order_service.controller;

import com.goktugakca.order_service.dto.CreateOrderRequest;
import com.goktugakca.order_service.event.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate;

    @Value("${app.kafka.order-topic}")
    private String orderTopic;

    private final AtomicLong idGenerator = new AtomicLong(1);

    @PostMapping
    public String createOrder(@RequestBody CreateOrderRequest request) {
        long orderId = idGenerator.getAndIncrement();
        OrderCreatedEvent event =
                new OrderCreatedEvent(orderId, request.productSku(), request.quantity());
        kafkaTemplate.send(orderTopic, String.valueOf(orderId), event);
        return "Order created and event published: " + event;
    }
}