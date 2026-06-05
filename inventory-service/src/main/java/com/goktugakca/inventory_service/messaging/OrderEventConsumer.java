package com.goktugakca.inventory_service.messaging;

import com.goktugakca.inventory_service.event.OrderCreatedEvent;
import com.goktugakca.inventory_service.model.InventoryItem;
import com.goktugakca.inventory_service.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderEventConsumer {

    private final InventoryRepository inventoryRepository;

    @KafkaListener(topics = "${app.kafka.order-topic}")
    @Transactional
    public void onOrderCreated(OrderCreatedEvent event) {
        log.info("Received OrderCreatedEvent: {}", event);
        Optional<InventoryItem> inventoryItem= inventoryRepository.findBySku(event.productSku());
        if(inventoryItem.isEmpty()){
            log.warn("{}Stoğu bulunamadı", event.productSku());
        }else{
            InventoryItem item = inventoryItem.get();
            if(item.getAvailableQuantity()<event.quantity())
                log.warn("Yeterli stok yok.");
            else{
                item.setAvailableQuantity(
                        item.getAvailableQuantity()-event.quantity());
                inventoryRepository.save(item);
                log.info("{} stoğundan {} adet düşüldü, kalan: {}",
                        item.getSku(), event.quantity(),
                        item.getAvailableQuantity());
            }

        }
    }
}