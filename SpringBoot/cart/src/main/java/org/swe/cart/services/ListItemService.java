package org.swe.cart.services;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.swe.cart.embeddables.ListItemKey;
import org.swe.cart.entities.Group;
import org.swe.cart.entities.Item;
import org.swe.cart.entities.ListItem;
import org.swe.cart.entities.ShopList;
import org.swe.cart.entities.User;
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


    public ListItem addItemToList(Integer groupId, Integer listId, Integer itemId, Integer quantity, Boolean communal){
        Group group = groupRepository.findById(groupId).orElseThrow();
        
        ShopList list = listRepository.findById(listId).orElseThrow();

        Item item = itemRepository.findById(itemId).orElseThrow();

        String username = (String) SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElseThrow();

        if(!group.getLists().contains(list)) return null;
        
        if(!listItemRepository.existsByListAndItem(list, item)) return null;
        
        ListItem listItem = new ListItem();
        listItem.setItem(item);
        listItem.setList(list);
        listItem.setQuantity(quantity);

        ListItemKey listItemKey = new ListItemKey();
        listItemKey.setItemid(itemId);
        listItemKey.setListid(listId);
        listItemKey.setUserid(user.getId());

        listItem.setId(listItemKey);
        listItem.setCommunal(communal);
        listItem = listItemRepository.save(listItem);

        return listItem;


    }

    public ResponseEntity<ListItem> updateListItem(Integer listId, Integer itemId, Integer quantity, Boolean communal, Boolean bought){
        Item item = itemRepository.findById(itemId).orElseThrow();
        ShopList list = listRepository.findById(listId).orElseThrow();

        ListItem listItem = listItemRepository.findByListAndItem(list, item);
        listItem.setQuantity(quantity);
        listItem.setCommunal(communal);
        listItem.setBought(bought);
        listItem = listItemRepository.save(listItem);
        return new ResponseEntity<>(listItem, HttpStatus.OK);
    }

    public ResponseEntity<ListItem> deleteListItem(Integer itemId, Integer listId){
        Item item = itemRepository.findById(itemId).orElseThrow();
        ShopList list = listRepository.findById(listId).orElseThrow();
        ListItem listItem = listItemRepository.findByListAndItem(list, item);
        listItemRepository.deleteByListAndItem(list, item);
        return new ResponseEntity<>(listItem, HttpStatus.OK);
    }

    public ResponseEntity<ListItem> buyItem(Integer listId, Integer itemId){
        ShopList list = listRepository.findById(listId).orElseThrow();
        Item item = itemRepository.findById(itemId).orElseThrow();
        ListItem listItem = listItemRepository.findByListAndItem(list, item);
        Integer quantity = listItem.getQuantity();
        Boolean communal = listItem.getCommunal();
        Boolean bought = true;
        return updateListItem(listId, itemId, quantity, communal, bought);
    }

}
