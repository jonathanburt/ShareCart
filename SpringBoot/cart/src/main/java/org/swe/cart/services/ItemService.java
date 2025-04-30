package org.swe.cart.services;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
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

    /**
     * gets all items belonging to a given group
     * @author Jeremy Bullis jab525@case.edu
     * @param groupId the ID of the group from which the items are being retrieved, found in the URL
     * @return list of Item data transfer objects
     */
    public List<ItemDTO> getItems(Integer groupId){
        Group group = groupRepository.findById(groupId).orElseThrow();
        List<Item> items = itemRepository.findByGroup(group);
        List<ItemDTO> dtos = new ArrayList<>();
        for(Item item : items){
            dtos.add(itemToItemDTO(item));
        }
        return dtos;
    }

    /**
     * creates a new item with given attributes, within the specified group
     * @author Jeremy Bullis jab525@case.edu
     * @param name the name of the object being created
     * @param description the description of the object
     * @param category the category into which the item falls
     * @param price the price of the item
     * @param groupId the ID of the group to which the item is being added, found in the URL
     * @return the data transfer object of the item being created
     */
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

    /**
     * formats a given time to HTTP
     * @author Jeremy Bullis jab525@case.edu
     * @param instant the time which will be converted to HTTP format
     * @return properly HTTP-formatted date time
     */
    private String formatInstantToHTTP(Instant instant) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
            "EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return dateFormat.format(Date.from(instant));
    }

    /**
     * converts an item to its corresponding data transfer object
     * @author Jeremy Bullis jab525@case.edu
     * @param item the item which will be converted to a data transfer object
     * @return the data transfer object corresponding to the given item
     */
    private ItemDTO itemToItemDTO(Item item){
        ItemDTO itemDTO = new ItemDTO(item.getId(), item.getName(), item.getDescription(), item.getCategory(), 
                        item.getPrice(), formatInstantToHTTP(item.getCreatedAt()));
        return itemDTO;
    }

    

    /**
     * updates a given item with new information
     * @author Jeremy Bullis jab525@case.edu
     * @param itemId the ID of the item to be updated, found in the URL
     * @param groupId the ID of the group to which the item belongs, found in the URL
     * @param name the new name of the item
     * @param description the new description of the item
     * @param category the new category of the item
     * @param price the new price of the item
     * @return the data transfer object corresponding to the item being updated
     * @throws GroupMismatchException
     */
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

    
    /**
     * deletes an item from its group
     * @author Jeremy Bullis jab525@case.edu
     * @param itemId the ID of the item being deleted, found in the URL
     * @param groupId the ID of the group from which the item is being deleted, found in the URL
     * @return message verifying that the item has been deleted
     * @throws GroupMismatchException
     */
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
