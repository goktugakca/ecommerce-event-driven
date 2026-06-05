package com.goktugakca.order_service.controller;

import com.goktugakca.order_service.dto.CreateOrderRequest;
import com.goktugakca.order_service.event.OrderCreatedEvent;
import com.goktugakca.order_service.model.Order;
import com.goktugakca.order_service.repository.OrderRepository;
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
    private final OrderRepository orderRepository;

    @Value("${app.kafka.order-topic}")
    private String orderTopic;

    @PostMapping
    public String createOrder(@RequestBody CreateOrderRequest request) {
        Order order = new Order();
        order.setQuantity(request.quantity());
        order.setProductSku(request.productSku());
        Order saved = orderRepository.save(order);
        OrderCreatedEvent event = new OrderCreatedEvent(saved.getId(),saved.getProductSku(), saved.getQuantity());
        kafkaTemplate.send(orderTopic,String.valueOf(saved.getId()), event);
        return "Order created and event published: " + event;
    }
}