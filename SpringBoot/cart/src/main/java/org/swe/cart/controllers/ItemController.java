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
import org.springframework.web.bind.annotation.RestController;
import org.swe.cart.entities.Item;
import org.swe.cart.entities.ListItem;
import org.swe.cart.entities.ShopList;
import org.swe.cart.entities.User;
import org.swe.cart.exceptions.GroupMismatchException;
import org.swe.cart.payload.AddItemToListDTO;
import org.swe.cart.payload.ChangeQuantityDTO;
import org.swe.cart.payload.ItemCreateDTO;
import org.swe.cart.payload.ItemDTO;
import org.swe.cart.payload.ListItemDTO;
import org.swe.cart.payload.UpdateItemDTO;
import org.swe.cart.repositories.ItemRepository;
import org.swe.cart.repositories.ListItemRepository;
import org.swe.cart.repositories.ListRepository;
import org.swe.cart.repositories.UserRepository;
import org.swe.cart.services.ItemService;
import org.swe.cart.services.ListItemService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(path="/api/group/{groupId}/item")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final ListItemService listItemService;
    private final UserRepository userRepository;
    private final ListItemRepository listItemRepository;
    private final ListRepository listRepository;
    private final ItemRepository itemRepository;
    
    @GetMapping("/getall")
    public ResponseEntity<List<Item>> getAllItems(@PathVariable Integer groupId) {
        List<Item> items = itemService.getItems(groupId);
        return ResponseEntity.ok(items);
    }

    @PostMapping("/create")
    public ResponseEntity<ItemDTO> createItem(@PathVariable Integer groupId, @RequestBody ItemCreateDTO itemCreateDTO) {
        String name = itemCreateDTO.getName();
        String description = itemCreateDTO.getName();
        String category = itemCreateDTO.getCategory();
        Float price = itemCreateDTO.getPrice();
        ItemDTO item = itemService.createItem(name, description, category, price, groupId);
        return ResponseEntity.status(HttpStatus.CREATED).body(item);
    }

    
    @PostMapping("/{listId}/add")
    public ResponseEntity<ListItemDTO> addItem(@PathVariable Integer groupId, @PathVariable Integer listId, @RequestBody AddItemToListDTO addItemToListDTO) {
        //TODO: process POST request
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findByUsername(username).orElseThrow();
        Integer userId = user.getId();
        Integer itemId = addItemToListDTO.getItemId();
        Integer quantity = addItemToListDTO.getQuantity();
        Boolean communal = addItemToListDTO.getCommunal();
        Boolean bought = addItemToListDTO.getBought();
        ListItemDTO listItemDTO = listItemService.addItemToList(groupId, listId, userId, itemId, quantity, bought, communal);
        if(listItemDTO == null) return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        return ResponseEntity.status(HttpStatus.CREATED).body(listItemDTO);
    }


    @PutMapping("/{itemId}/update")
    public ResponseEntity<ItemDTO> updateItem(@PathVariable Integer itemId, @PathVariable Integer groupId, @RequestBody UpdateItemDTO updateItemDTO) throws GroupMismatchException {
        //TODO: process PUT request
        String name = updateItemDTO.getName();
        String description = updateItemDTO.getDescription();
        String category = updateItemDTO.getCategory();
        Float price = updateItemDTO.getPrice();
        ItemDTO itemDTO = itemService.updateItem(itemId, groupId, name, description, category, price);
        return ResponseEntity.status(HttpStatus.CREATED).body(itemDTO);
    }

    @PutMapping("/{listId}/{itemId}/buy")
    @PreAuthorize("hasAuthority('ROLE_ADMIN_GROUP_' + #groupId) or hasAuthority('ROLE_SHOPPER_GROUP_' + #groupId)")
    public ResponseEntity<ListItemDTO> buyItem(@PathVariable Integer groupId, @PathVariable Integer listId, @PathVariable Integer itemId){
        ShopList list = listRepository.findById(listId).orElseThrow();
        Item item = itemRepository.findById(itemId).orElseThrow();
        ListItem listItem = listItemRepository.findByListAndItem(list, item);
        ListItemDTO listItemDTO = listItemService.updateListItem(listId, itemId, listItem.getQuantity(), listItem.getCommunal(), true);
        return ResponseEntity.status(HttpStatus.CREATED).body(listItemDTO);
    }


    @PutMapping("/{listId}/{itemId}/quantity")
    @PreAuthorize("hasAuthority('ROLE_ADMIN_GROUP_' + #groupId) or hasAuthority('ROLE_SHOPPER_GROUP_' + #groupId) or hasAuthority('ROLE_MEMBER_GROUP_' + #groupId)")
    public ResponseEntity<ListItemDTO> changeQuantity(@PathVariable Integer listId, @PathVariable Integer groupId, @PathVariable Integer itemId, @RequestBody ChangeQuantityDTO changeQuantityDTO){
        ShopList list = listRepository.findById(listId).orElseThrow();
        Item item = itemRepository.findById(itemId).orElseThrow();
        ListItem listItem = listItemRepository.findByListAndItem(list, item);
        Integer quantity = changeQuantityDTO.getQuantity();
        ListItemDTO listItemDTO = listItemService.updateListItem(listId, itemId, quantity, listItem.getCommunal(), listItem.getBought());
        return ResponseEntity.status(HttpStatus.OK).body(listItemDTO);
    }

    @PutMapping("/{listId}/{itemId}/remove")
    @PreAuthorize("hasAuthority('ROLE_ADMIN_GROUP_' + #groupId) or hasAuthority('ROLE_SHOPPER_GROUP_' + #groupId)")
    public String removeItemFromList(@PathVariable Integer listId, @PathVariable Integer itemId, @PathVariable Integer groupId){
        return listItemService.deleteListItem(itemId, listId);
    }

    @DeleteMapping("/{itemId}/delete")
    @PreAuthorize("hasAuthority('ROLE_ADMIN_GROUP_' + #groupId) or hasAuthority('ROLE_SHOPPER_GROUP_' + #groupId)")
    public String deleteItem(@PathVariable Integer itemId, @PathVariable Integer groupId) throws GroupMismatchException{
        return itemService.deleteItem(itemId, groupId);
    }
    
}
