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

    public List<ListItem> getListItems(Integer listId){
        ShopList list = listRepository.findById(listId).orElseThrow();
        return listItemRepository.findByList(list);
    }


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
        listItem.setCommunal(communal);
        listItem = listItemRepository.save(listItem);

        ListItemDTO listItemDTO = listItemToListItemDTO(listItem);

        return listItemDTO;


    }

    private String formatInstantToHTTP(Instant instant) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
            "EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return dateFormat.format(Date.from(instant));
    }

    private ListItemDTO listItemToListItemDTO(ListItem listItem){
        ListItemDTO listItemDTO = new ListItemDTO(listItem.getItem().getId(), listItem.getList().getId(), listItem.getUser().getId(), listItem.getCommunal(), listItem.getQuantity(), listItem.getBought(), formatInstantToHTTP(listItem.getCreatedAt()));
        return listItemDTO;
    }

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

    public String deleteListItem(Integer itemId, Integer listId){
        Item item = itemRepository.findById(itemId).orElseThrow();
        ShopList list = listRepository.findById(listId).orElseThrow();
        listItemRepository.deleteByListAndItem(list, item);
        return "Item removed from list";
    }

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
