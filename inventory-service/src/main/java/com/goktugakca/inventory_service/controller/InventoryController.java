package com.goktugakca.inventory_service.controller;

import com.goktugakca.inventory_service.model.InventoryItem;
import com.goktugakca.inventory_service.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryRepository inventoryRepository;

    @GetMapping
    public List<InventoryItem> findAll(){
        return inventoryRepository.findAll();
    }
}
