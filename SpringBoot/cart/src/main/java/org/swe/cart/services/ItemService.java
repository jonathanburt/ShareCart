package org.swe.cart.services;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.springframework.stereotype.Service;
import org.swe.cart.entities.Group;
import org.swe.cart.entities.Item;
import org.swe.cart.exceptions.GroupMismatchException;
import org.swe.cart.payload.ItemDTO;
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

    public ItemDTO createItem(String name, String description, String category, Float price, Integer groupId){
        Group group = groupRepository.findById(groupId).orElseThrow();
        Item item = new Item();
        item.setName(name);
        item.setDescription(description);
        item.setCategory(category);
        item.setPrice(price);
        item.setGroup(group);
        item = itemRepository.save(item);
        ItemDTO itemDTO = itemToItemDTO(item);
        return itemDTO;

    }

    private String formatInstantToHTTP(Instant instant) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
            "EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return dateFormat.format(Date.from(instant));
    }

    private ItemDTO itemToItemDTO(Item item){
        ItemDTO itemDTO = new ItemDTO(item.getId(), item.getName(), item.getDescription(), item.getCategory(), 
                        item.getPrice(), item.getGroup(), formatInstantToHTTP(item.getCreatedAt()));
        return itemDTO;
    }

    

    public ItemDTO updateItem(Integer itemId, Integer groupId, String name, String description, String category, Float price) throws GroupMismatchException{
        Group group = groupRepository.findById(groupId).orElseThrow();
        Item item = itemRepository.findByIdAndGroup(itemId, group).orElseThrow();
        if(!group.equals(item.getGroup())){
            throw new GroupMismatchException("Group and Item do not match");
        }
        item.setGroup(group);
        item.setName(name);
        item.setDescription(description);
        item.setCategory(category);
        item.setPrice(price);
        item = itemRepository.save(item);
        return itemToItemDTO(item);
    }

    

    public String deleteItem(Integer itemId, Integer groupId) throws GroupMismatchException{
        Group group = groupRepository.findById(groupId).orElseThrow();
        Item item = itemRepository.findByIdAndGroup(itemId, group).orElseThrow();
        if(!group.equals(item.getGroup())){
            throw new GroupMismatchException("Group and Item do not match");
        }
        itemRepository.deleteById(itemId);
        return "Item deleted";
    }

    
}
