package com.goktugakca.inventory_service.messaging;

import com.goktugakca.inventory_service.event.OrderCreatedEvent;
import com.goktugakca.inventory_service.model.InventoryItem;
import com.goktugakca.inventory_service.repository.InventoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderEventConsumerTest {

    @Mock
    private InventoryRepository inventoryRepository;

    @InjectMocks
    private OrderEventConsumer orderEventConsumer;

    @Test
    void stokYeterliyseDusurulur() {
        // given — sahte verimizi hazırlıyoruz
        InventoryItem item = new InventoryItem();
        item.setSku("LAPTOP-001");
        item.setAvailableQuantity(50);
        when(inventoryRepository.findBySku("LAPTOP-001")).thenReturn(Optional.of(item));

        OrderCreatedEvent event = new OrderCreatedEvent(1L, "LAPTOP-001", 3);

        // when — test ettiğimiz metodu çağırıyoruz
        orderEventConsumer.onOrderCreated(event);

        // then — sonucu doğruluyoruz
        assertThat(item.getAvailableQuantity()).isEqualTo(47);
        verify(inventoryRepository).save(item);
    }
}