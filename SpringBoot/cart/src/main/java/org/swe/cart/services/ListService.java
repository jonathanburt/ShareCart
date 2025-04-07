package org.swe.cart.services;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.TimeZone;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.swe.cart.entities.Group;
import org.swe.cart.entities.ShopList;
import org.swe.cart.exceptions.GroupDoesNotExistException;
import org.swe.cart.exceptions.ListAlreadyAddedToGroupException;
import org.swe.cart.payload.ListItemDTO;
import org.swe.cart.payload.ShopListDTO;
import org.swe.cart.repositories.GroupRepository;
import org.swe.cart.repositories.ListRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ListService {
    private final GroupRepository groupRepository;
    private final ListRepository listRepository;

    public ShopListDTO addListToGroup(Integer groupId, String name) throws GroupDoesNotExistException, ListAlreadyAddedToGroupException{
        Optional<Group> optionalGroup = groupRepository.findById(groupId);
        if(optionalGroup.isEmpty()) throw new GroupDoesNotExistException("Group does not exist");
        
        Group group = optionalGroup.get();

        if(listRepository.existsByGroupAndName(group, name)) throw new ListAlreadyAddedToGroupException("List already added to group");

        ShopList list = new ShopList();
        list.setGroup(group);
        list.setName(name);
        list.setItems(new HashSet<>());

        //TODO Handle potential ConstraintViolationException
        listRepository.save(list);

        return shopListToShopListDTO(list); 
    }

    public String deleteList(Integer listId){
        ShopList list = listRepository.findById(listId).orElseThrow();
        listRepository.deleteById(listId);
        return "List deleted";
    }

    public ShopListDTO updateList(Integer listId, String name, Integer groupId){
        ShopList list = listRepository.findById(listId).orElseThrow();
        Group group = groupRepository.findById(groupId).orElseThrow();
        list.setName(name);
        list.setGroup(group);
        list = listRepository.save(list);
        ShopListDTO shopListDTO = shopListToShopListDTO(list);
        return shopListDTO;
    }

    public List<ShopListDTO> getAllLists(Integer groupId){
        Group group = groupRepository.findById(groupId).orElseThrow();
        List<ShopList> lists = listRepository.findAllByGroup(group);

        return lists.stream()
        .map(list -> shopListToShopListDTO(list)).collect(Collectors.toList());
    }

    public ShopListDTO getList(Integer listId){
        ShopList list = listRepository.findById(listId).orElseThrow();
        return shopListToShopListDTO(list);
    }

    private ShopListDTO shopListToShopListDTO(ShopList list){
        return new ShopListDTO(list.getName(), list.getId(),list.getGroup().getId(),formatInstantToHTTP(list.getCreatedAt()),
                    list.getItems().stream().map(
                        listItem -> new ListItemDTO(
                            listItem.getId().getItemid(), 
                            list.getId(), 
                            listItem.getId().getUserid(), 
                            listItem.getCommunal(), 
                            listItem.getQuantity(), 
                            listItem.getBought(),
                            formatInstantToHTTP(listItem.getCreatedAt()))
                    ).collect(Collectors.toList()));
    }
    private String formatInstantToHTTP(Instant instant) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
            "EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return dateFormat.format(Date.from(instant));
    }
}
