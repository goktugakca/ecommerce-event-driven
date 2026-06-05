package com.goktugakca.inventory_service;

import com.goktugakca.inventory_service.model.InventoryItem;
import com.goktugakca.inventory_service.repository.InventoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class InventoryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryServiceApplication.class, args);
	}
	@Bean
    CommandLineRunner seedInventory(InventoryRepository repository) {
		return args -> {
			if (repository.count() == 0) {
				InventoryItem laptop = new InventoryItem();
				laptop.setSku("LAPTOP-001");
				laptop.setAvailableQuantity(50);
				repository.save(laptop);

				InventoryItem phone = new InventoryItem();
				phone.setSku("PHONE-002");
				phone.setAvailableQuantity(30);
				repository.save(phone);
			}
		};
	}
}
