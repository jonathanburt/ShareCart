package org.swe.cart.services;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.swe.cart.embeddables.ListItemKey;
import org.swe.cart.entities.Group;
import org.swe.cart.entities.Item;
import org.swe.cart.entities.ListItem;
import org.swe.cart.entities.ShopList;
import org.swe.cart.entities.User;
import org.swe.cart.payload.ListItemDTO;
import org.swe.cart.repositories.GroupRepository;
import org.swe.cart.repositories.ItemRepository;
import org.swe.cart.repositories.ListItemRepository;
import org.swe.cart.repositories.ListRepository;
import org.swe.cart.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ListItemService {

    private final GroupRepository groupRepository;
    private final ListRepository listRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final ListItemRepository listItemRepository;

    /**
     * gets a list of all items from a given list
     * @author Jeremy Bullis jab525@case.edu
     * @param listId the ID of the list from which the items are being retrieved, found in the URL
     * @return list of the ListItems contained in the given list
     */
    public List<ListItem> getListItems(Integer listId){
        ShopList list = listRepository.findById(listId).orElseThrow();
        return listItemRepository.findByList(list);
    }


    /**
     * adds an already-created item to a list
     * @author Jeremy Bullis jab525@case.edu
     * @param groupId the ID of the group to which the list belongs, where the user is adding the item, found in the URL
     * @param listId the ID of the list to which the item is being added, found in the URL
     * @param userId the ID of the current user
     * @param itemId the ID of the item which is being added to the list, found in the URL
     * @param quantity the quantity that the user wants of the item
     * @param bought whether or not the item has been bought (defaults to false)
     * @param communal whether the item is for an individual or for a group
     * @return data transfer object corresponding to the list item
     */
    public ListItemDTO addItemToList(Integer groupId, Integer listId, Integer userId, Integer itemId, Integer quantity, Boolean bought, Boolean communal){
        Group group = groupRepository.findById(groupId).orElseThrow();
        
        ShopList list = listRepository.findById(listId).orElseThrow();

        Item item = itemRepository.findById(itemId).orElseThrow();

        String username = (String) SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElseThrow();

        if(!group.getLists().contains(list)) return null;
        
        if(listItemRepository.existsByListAndItem(list, item)) return null;
        
        ListItem listItem = new ListItem();
        listItem.setItem(item);
        listItem.setList(list);
        listItem.setUser(user);
        listItem.setQuantity(quantity);
        listItem.setBought(bought);

        ListItemKey listItemKey = new ListItemKey();
        listItemKey.setItemid(itemId);
        listItemKey.setListid(listId);
        listItemKey.setUserid(user.getId());

        listItem.setId(listItemKey);
        System.err.println(listItemKey.hashCode());
        listItem.setCommunal(communal);
        listItem = listItemRepository.save(listItem);

        ListItemDTO listItemDTO = listItemToListItemDTO(listItem);

        return listItemDTO;


    }

    /**
     * formats a given time to HTTP
     * @author Jeremy Bullis jab525@case.edu
     * @param instant the time which will be converted to HTTP format
     * @return the correctly HTTP-formatted date time
     */
    private String formatInstantToHTTP(Instant instant) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
            "EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return dateFormat.format(Date.from(instant));
    }

    /**
     * converts a given List Item to the corresponding data transfer object
     * @author Jeremy Bullis jab525@case.edu
     * @param listItem the list item whose data transfer object we want
     * @return the data transfer object corresponding to the given list item
     */
    private ListItemDTO listItemToListItemDTO(ListItem listItem){
        ListItemDTO listItemDTO = new ListItemDTO(listItem.getItem().getId(), listItem.getList().getId(), listItem.getUser().getId(), listItem.getCommunal(), listItem.getQuantity(), listItem.getBought(), formatInstantToHTTP(listItem.getCreatedAt()));
        return listItemDTO;
    }

    /**
     * updates the information for a given List Item
     * @author Jeremy Bullis jab525@case.edu
     * @param listId the ID of the list to which the item belongs, found in the URL
     * @param itemId the ID of the item being updated, found in the URL
     * @param quantity the new quantity of the item
     * @param communal whether the item is for an individual or the group as a whole
     * @param bought whether or not the item has been bought
     * @return the data transfer object corresponding to the list item
     */
    public ListItemDTO updateListItem(Integer listId, Integer itemId, Integer quantity, Boolean communal, Boolean bought){
        Item item = itemRepository.findById(itemId).orElseThrow();
        ShopList list = listRepository.findById(listId).orElseThrow();

        ListItem listItem = listItemRepository.findByListAndItem(list, item);
        listItem.setQuantity(quantity);
        listItem.setCommunal(communal);
        listItem.setBought(bought);
        listItem = listItemRepository.save(listItem);
        ListItemDTO listItemDTO = listItemToListItemDTO(listItem);
        return listItemDTO;
    }

    /**
     * removes a given item from a list
     * @author Jeremy Bullis jab525@case.edu
     * @param itemId the ID of the item being removed from the list, found in the URL
     * @param listId the ID of the list from which the item is being removed, found in the URL
     * @return a message verifying that the item has been removed from the list
     */
    public String deleteListItem(Integer itemId, Integer listId){
        Item item = itemRepository.findById(itemId).orElseThrow();
        ShopList list = listRepository.findById(listId).orElseThrow();
        listItemRepository.deleteByListAndItem(list, item);
        return "Item removed from list";
    }

    /**
     * updates a given list item to reflect that it has been bought
     * @author Jeremy Bullis jab525@case.edu
     * @param listId the ID of the list to which the item belongs, found in the URL
     * @param itemId the ID of the item being bought, found in the URL
     * @return the data transfer object of the item being bought
     */
    public ListItemDTO buyItem(Integer listId, Integer itemId){
        ShopList list = listRepository.findById(listId).orElseThrow();
        Item item = itemRepository.findById(itemId).orElseThrow();
        ListItem listItem = listItemRepository.findByListAndItem(list, item);
        Integer quantity = listItem.getQuantity();
        Boolean communal = listItem.getCommunal();
        Boolean bought = true;
        return updateListItem(listId, itemId, quantity, communal, bought);
    }

}
