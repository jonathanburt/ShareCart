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
    
    /**
     * this method only performs actions on the network layer, and calls Item Service for the rest
     * @author Jeremy Bullis jab525@case.edu
     * @param groupId the ID of the group to which the user is retrieving the items, found in the URL
     * @return a formatted http response with information about the items that the method is retrieving from the specified group
     */
    @GetMapping("/getall")
    @PreAuthorize("hasAuthority('ROLE_ADMIN_GROUP_' + #groupId) or hasAuthority('ROLE_SHOPPER_GROUP_' + #groupId) or hasAuthority('ROLE_MEMBER_GROUP_' + #groupId)")
    public ResponseEntity<List<ItemDTO>> getAllItems(@PathVariable Integer groupId) {
        List<ItemDTO> items = itemService.getItems(groupId);
        return ResponseEntity.ok(items);
    }

    /**
     * this method performs actions only on the network layer and calls to ItemService
     * @author Jeremy Bullis jab525@case.edu
     * @param groupId the ID of the group to which the use is adding the item, found in the URL
     * @param itemCreateDTO the data transfer object representing the item that the user is creating, containing the item's name, description, category, and price
     * @return a formatted http response containing information about the new item that was created
     */
    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ROLE_ADMIN_GROUP_' + #groupId) or hasAuthority('ROLE_SHOPPER_GROUP_' + #groupId)")
    public ResponseEntity<ItemDTO> createItem(@PathVariable Integer groupId, @RequestBody ItemCreateDTO itemCreateDTO) {
        String name = itemCreateDTO.getName();
        String description = itemCreateDTO.getDescription();
        String category = itemCreateDTO.getCategory();
        Float price = itemCreateDTO.getPrice();
        ItemDTO item = itemService.createItem(name, description, category, price, groupId);
        return ResponseEntity.status(HttpStatus.CREATED).body(item);
    }

    /**
     * this method performs actions on the network layer and calls to ListItemService
     * @author Jeremy Bullis jab525@case.edu
     * @param groupId the ID of the group to which the user is adding the item, found in the URL
     * @param listId the ID of the list to which the user is adding the item, found in the URL
     * @param addItemToListDTO the data transfer objecting representing the addition of the item to the list, containing the item ID, the quantity, whether the item is communal, and whether the item has been bought
     * @return a formatted http response containing the list item data transfer object
     */
    @PostMapping("/{listId}/add")
    @PreAuthorize("hasAuthority('ROLE_ADMIN_GROUP_' + #groupId) or hasAuthority('ROLE_SHOPPER_GROUP_' + #groupId) or hasAuthority('ROLE_MEMBER_GROUP_' + #groupId)")
    public ResponseEntity<ListItemDTO> addItemToList(@PathVariable Integer groupId, @PathVariable Integer listId, @RequestBody AddItemToListDTO addItemToListDTO) {
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


    /**
     * this method performs actions only on the network layer and calls to ItemService for the rest
     * @author Jeremy Bullis jab525@case.edu
     * @param itemId the ID of the item which is being updated, found in the URL
     * @param groupId the ID of the group to which the item belongs, also found in the URL
     * @param updateItemDTO a data transfer object containing the item's name, description, category, and price
     * @return formatted http response containing the data transfer object corresponding to the item being updated
     * @throws GroupMismatchException
     */
    @PutMapping("/{itemId}/update")
    @PreAuthorize("hasAuthority('ROLE_ADMIN_GROUP_' + #groupId) or hasAuthority('ROLE_SHOPPER_GROUP_' + #groupId)")
    public ResponseEntity<ItemDTO> updateItem(@PathVariable Integer itemId, @PathVariable Integer groupId, @RequestBody UpdateItemDTO updateItemDTO) throws GroupMismatchException {
        //TODO: process PUT request
        String name = updateItemDTO.getName();
        String description = updateItemDTO.getDescription();
        String category = updateItemDTO.getCategory();
        Float price = updateItemDTO.getPrice();
        ItemDTO itemDTO = itemService.updateItem(itemId, groupId, name, description, category, price);
        return ResponseEntity.status(HttpStatus.CREATED).body(itemDTO);
    }

    /**
     * this method performs actions only on the network layer and calls ListItemService for the rest
     * @author Jeremy Bullis jab525@case.edu
     * @param groupId the ID of the group to which the specified item belongs, found in the URL
     * @param listId the ID of the list to which the specified item belongs, found in the URL
     * @param itemId the ID of the item being bought, found in the URL
     * @return formatted http response containing the data transfer object corresponding to the specific list item
     */
    @PutMapping("/{listId}/{itemId}/buy")
    @PreAuthorize("hasAuthority('ROLE_ADMIN_GROUP_' + #groupId) or hasAuthority('ROLE_SHOPPER_GROUP_' + #groupId)")
    public ResponseEntity<ListItemDTO> buyItem(@PathVariable Integer groupId, @PathVariable Integer listId, @PathVariable Integer itemId){
        ShopList list = listRepository.findById(listId).orElseThrow();
        Item item = itemRepository.findById(itemId).orElseThrow();
        ListItem listItem = listItemRepository.findByListAndItem(list, item);
        ListItemDTO listItemDTO = listItemService.updateListItem(listId, itemId, listItem.getQuantity(), listItem.getCommunal(), true);
        return ResponseEntity.status(HttpStatus.CREATED).body(listItemDTO);
    }

    /**
     * this method performs actions only on the network layer and calls listItemService for the rest
     * @author Jeremy Bullis jab525@case.edu
     * @param listId the ID of the list to which the specified item belongs, found in the URL
     * @param groupId the ID of the group to which the specified item belongs, found in the URL
     * @param itemId the ID of the item whose quantity is being changed, found in the URL
     * @param changeQuantityDTO data transfer object containing the quantity of the item
     * @return formatted http response containing the specified ListItem's data transfer object
     */
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

    /**
     * this method performs actions only on the network layer and calls ListItemService for the rest
     * @author Jeremy Bullis jab525@case.edu
     * @param listId the ID of the list from which the item is being removed, found in the URL
     * @param itemId the ID of the item being removed from the list, found in the URL
     * @param groupId the ID of the group to which the item belongs, found in the URL
     * @return calls deleteListItem from ListItemService, which returns a message verifying that the item has been deleted from the list
     */
    @PutMapping("/{listId}/{itemId}/remove")
    @PreAuthorize("hasAuthority('ROLE_ADMIN_GROUP_' + #groupId) or hasAuthority('ROLE_SHOPPER_GROUP_' + #groupId)")
    public String removeItemFromList(@PathVariable Integer listId, @PathVariable Integer itemId, @PathVariable Integer groupId){
        return listItemService.deleteListItem(itemId, listId);
    }

    /**
     * this method performs actions only on the network layer and calls ItemService for the rest
     * @author Jeremy Bullis jab525@case.edu
     * @param itemId the ID of the item being deleted, found in the URL
     * @param groupId the ID of the group from which the item is being deleted, found in the URL
     * @return calls deleteItem from ItemService, which returns a message verifying that the item has been deleted from the group
     * @throws GroupMismatchException
     */
    @DeleteMapping("/{itemId}/delete")
    @PreAuthorize("hasAuthority('ROLE_ADMIN_GROUP_' + #groupId) or hasAuthority('ROLE_SHOPPER_GROUP_' + #groupId)")
    public String deleteItem(@PathVariable Integer itemId, @PathVariable Integer groupId) throws GroupMismatchException{
        return itemService.deleteItem(itemId, groupId);
    }
    
}
