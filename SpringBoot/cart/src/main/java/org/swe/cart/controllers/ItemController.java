package org.swe.cart.controllers;

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
import org.swe.cart.entities.ListItem;
import org.swe.cart.services.ItemService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(path="/api/group/{groupId}/item")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    
    @GetMapping("/getall")
    public String getAllItems(@PathVariable Integer groupId, @RequestParam String param) {

        return new String();
    }
    
    @PostMapping("/add")
    public ResponseEntity<ListItem> addItem(@PathVariable Integer groupId, @PathVariable Integer listId, @PathVariable Integer itemId, @PathVariable Integer quantity, @PathVariable Boolean communal, @RequestBody String entity) {
        //TODO: process POST request
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ListItem item = itemService.addItemToList(groupId, listId, itemId, quantity, communal);
        if(item == null) return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        return ResponseEntity.status(HttpStatus.CREATED).body(item);
        
    }

    @PutMapping("/{itemId}/update")
    public String updateItem(@PathVariable Integer itemId, @RequestBody String entity) {
        //TODO: process PUT request
        
        return entity;
    }
    
}
