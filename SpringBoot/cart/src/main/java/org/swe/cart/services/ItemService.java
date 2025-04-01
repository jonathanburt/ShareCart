package org.swe.cart.services;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.swe.cart.entities.Group;
import org.swe.cart.entities.Item;
import org.swe.cart.repositories.GroupRepository;
import org.swe.cart.repositories.ItemRepository;
import org.swe.cart.repositories.ListItemRepository;
import org.swe.cart.repositories.ListRepository;
import org.swe.cart.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final GroupRepository groupRepository;
    private final ListRepository listRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final ListItemRepository listItemRepository;

    public List<Item> getItems(Integer groupId){
        Group group = groupRepository.findById(groupId).orElseThrow();
        return itemRepository.findByGroup(group);
    }

    public Item createItem(String name, String description, String category, Float price, Integer groupId){
        Group group = groupRepository.findById(groupId).orElseThrow();
        Item item = new Item();
        item.setName(name);
        item.setDescription(description);
        item.setCategory(category);
        item.setPrice(price);
        item.setGroup(group);
        item = itemRepository.save(item);
        return item;

    }

    

    public ResponseEntity<Item> updateItem(Integer itemId, Integer groupId, String name, String description, String category, Float price){
        Group group = groupRepository.findById(groupId).orElseThrow();
        Item item = itemRepository.findByIdAndGroup(itemId, group).orElseThrow();
        if(!groupId.equals(item.getId())){
            return new ResponseEntity<Item>(item, HttpStatus.BAD_REQUEST);
        }
        item.setName(name);
        item.setDescription(description);
        item.setCategory(category);
        item.setPrice(price);
        item = itemRepository.save(item);
        return new ResponseEntity<>(item, HttpStatus.OK);
    }

    

    public ResponseEntity<Item> deleteItem(Integer itemId, Integer groupId){
        Group group = groupRepository.findById(groupId).orElseThrow();
        Item item = itemRepository.findByIdAndGroup(itemId, group).orElseThrow();
        if(!groupId.equals(item.getId())){
            return new ResponseEntity<Item>(item, HttpStatus.BAD_REQUEST);
        }
        itemRepository.deleteById(itemId);
        return new ResponseEntity<>(item, HttpStatus.OK);
    }

    
}
