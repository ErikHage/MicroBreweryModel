package com.tfr.microbrew.dao;

import com.tfr.microbrew.config.InventoryConfig;
import com.tfr.microbrew.model.InventoryItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


/**
 *
 *
 * Created by Erik on 4/22/2017.
 */
@Repository("LocalInventoryDao")
public class LocalInventoryDao implements InventoryDao {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final Map<String, InventoryItem> INVENTORY = new HashMap<>();

    @Autowired
    public LocalInventoryDao(@Qualifier("InventoryItems") Set<InventoryItem> inventoryItems) {
        inventoryItems.forEach(item -> INVENTORY.put(item.getName(), item));
        logger.debug(String.format("Loaded %s inventory items from config", INVENTORY.size()));
    }

    @Override
    public void create(InventoryItem inventoryItem) {
        INVENTORY.put(inventoryItem.getName(), inventoryItem);
    }

    @Override
    public Set<InventoryItem> readAll() {
        return INVENTORY.entrySet().stream()
                .map(Map.Entry::getValue)
                .collect(Collectors.toSet());
    }

    @Override
    public InventoryItem readByName(String name) {
        return INVENTORY.get(name);
    }

    @Override
    public void update(InventoryItem inventoryItem) {
        INVENTORY.put(inventoryItem.getName(), inventoryItem);
    }

    @Override
    public void delete(InventoryItem inventoryItem) {
        INVENTORY.remove(inventoryItem.getName());
    }
}
