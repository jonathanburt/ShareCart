package org.swe.cart.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.swe.cart.entities.Item;
import org.swe.cart.exceptions.GroupMismatchException;
import org.swe.cart.payload.ItemDTO;
import org.swe.cart.payload.ListItemDTO;
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

    @PostMapping("/create")
    public ResponseEntity<ItemDTO> createItem(
        @PathVariable Integer groupId,
        @RequestParam String name,
        @RequestParam String description,
        @RequestParam String category,
        @RequestParam Float price) {
    
    ItemDTO item = itemService.createItem(name, description, category, price, groupId);
    return ResponseEntity.status(HttpStatus.CREATED).body(item);
    }

    
    @PostMapping("/{listId}/add")
    public ResponseEntity<ListItemDTO> addItem(@PathVariable Integer groupId, @PathVariable Integer listId, @RequestParam Integer itemId, @RequestParam Integer quantity, @RequestParam Boolean bought, @RequestParam Boolean communal) {
        //TODO: process POST request
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ListItemDTO listItemDTO = listItemService.addItemToList(groupId, listId, itemId, quantity, bought, communal);
        if(listItemDTO == null) return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        return ResponseEntity.status(HttpStatus.CREATED).body(listItemDTO);
    }


    @PutMapping("/{itemId}/update")
    public ResponseEntity<ItemDTO> updateItem(@PathVariable Integer itemId, @PathVariable Integer groupId, String name, String description, String category, Float price, @RequestBody String entity) throws GroupMismatchException {
        //TODO: process PUT request
        ItemDTO itemDTO = itemService.updateItem(itemId, groupId, name, description, category, price);
        return ResponseEntity.status(HttpStatus.CREATED).body(itemDTO);
    }

    @DeleteMapping("/{itemId}/delete")
    @PreAuthorize("hasAuthority('ROLE_ADMIN_GROUP_' + #groupId) or hasAuthority('ROLE_SHOPPER_GROUP_' + #groupId)")
    public String deleteItem(@PathVariable Integer itemId, @PathVariable Integer groupId) throws GroupMismatchException{
        return itemService.deleteItem(itemId, groupId);
    }
    
}
