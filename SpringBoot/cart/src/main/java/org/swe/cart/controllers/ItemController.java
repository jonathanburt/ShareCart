package org.swe.cart.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.swe.cart.entities.Item;
import org.swe.cart.entities.ListItem;
import org.swe.cart.services.ItemService;
import org.swe.cart.services.ListItemService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(path="/api/group/{groupId}/item")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final ListItemService listItemService;
    
    @GetMapping("/getall")
    public ResponseEntity<List<Item>> getAllItems(@PathVariable Integer groupId, @RequestParam String param) {
        List<Item> items = itemService.getItems(groupId);
        return ResponseEntity.ok(items);
    }
    
    @PostMapping("/add")
    public ResponseEntity<ListItem> addItem(@PathVariable Integer groupId, Integer listId, Integer itemId, Integer quantity, Boolean communal, @RequestBody String entity) {
        //TODO: process POST request
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ListItem item = listItemService.addItemToList(groupId, listId, itemId, quantity, communal);
        if(item == null) return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        return ResponseEntity.status(HttpStatus.CREATED).body(item);
        
    }

    @PutMapping("/{itemId}/update")
    public ResponseEntity<Item> updateItem(@PathVariable Integer itemId, String name, String description, String category, Float price, @RequestBody String entity) {
        //TODO: process PUT request
        return itemService.updateItem(itemId, name, description, category, price);
    }

    @PutMapping("/{itemId}/delete")
    public ResponseEntity<Item> deleteItem(@PathVariable Integer itemId){
        return itemService.deleteItem(itemId);
    }
    
}
