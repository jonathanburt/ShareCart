package org.swe.cart.services;

import java.util.Optional;

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
public class ItemService {
    private final GroupRepository groupRepository;
    private final ListRepository listRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final ListItemRepository listItemRepository;

    public ResponseEntity<ListItem> addItemToList(Integer groupId, Integer listId, Integer itemId, Integer quantity, Boolean communal){
        Optional<Group> optionalGroup = groupRepository.findById(groupId);
        if(optionalGroup.isEmpty()) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        Group group = optionalGroup.get();

        Optional<ShopList> optionalList = listRepository.findById(listId);
        if(optionalList.isEmpty()) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        ShopList list = optionalList.get();

        Optional<Item> optionalItem = itemRepository.findById(itemId);
        if(optionalList.isEmpty()) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        Item item = optionalItem.get();

        String username = (String) SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> optionalUser = userRepository.findByUsername(username);
        User user = optionalUser.get();

        if(!group.getLists().contains(list)) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        
        if(!listItemRepository.existsByListAndItem(list, item)) return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        
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

        return ResponseEntity.ok(listItem);


    }
}
