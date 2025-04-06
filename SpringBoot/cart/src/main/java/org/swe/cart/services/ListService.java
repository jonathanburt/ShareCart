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

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.swe.cart.entities.Group;
import org.swe.cart.entities.ShopList;
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

    public ResponseEntity<ShopList> addListToGroup(Integer groupId, String name){
        Optional<Group> optionalGroup = groupRepository.findById(groupId);
        if(optionalGroup.isEmpty()) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        
        Group group = optionalGroup.get();

        if(listRepository.existsByGroupAndName(group, name)) return new ResponseEntity<>(null, HttpStatus.CONFLICT);

        ShopList list = new ShopList();
        list.setGroup(group);
        list.setName(name);
        list.setItems(new HashSet<>());

        //TODO Handle potential ConstraintViolationException
        listRepository.save(list);

        return new ResponseEntity<>(list, HttpStatus.CREATED);  //Change this to a better return type
    }

    public ResponseEntity<ShopList> deleteList(Integer listId){
        ShopList list = listRepository.findById(listId).orElseThrow();
        listRepository.deleteById(listId);
        return new ResponseEntity<ShopList>(list, HttpStatus.OK);
    }

    public ResponseEntity<ShopList> updateList(Integer listId, String name, Integer groupId){
        ShopList list = listRepository.findById(listId).orElseThrow();
        Group group = groupRepository.findById(groupId).orElseThrow();
        list.setName(name);
        list.setGroup(group);
        list = listRepository.save(list);
        return new ResponseEntity<ShopList>(list, HttpStatus.OK);
    }

    public List<ShopListDTO> getAllLists(Integer groupId){
        Group group = groupRepository.findById(groupId).orElseThrow();
        List<ShopList> lists = listRepository.findAllByGroup(group);

        List<ShopListDTO> listDTOs = lists.stream()
        .map(list -> new ShopListDTO(
                list.getName(), 
                list.getId(), 
                list.getGroup().getId(), 
                formatInstantToHTTP(list.getCreatedAt()),
                list.getItems().stream().map(
                    listItem -> new ListItemDTO(
                        listItem.getItem().getId(),
                        list.getId(),
                        listItem.getUser().getId(),
                        listItem.getCommunal(),
                        listItem.getQuantity(),
                        formatInstantToHTTP(listItem.getCreatedAt())
                    )).collect(Collectors.toList())
                )).collect(Collectors.toList());

        return listDTOs;
    }

    private String formatInstantToHTTP(Instant instant) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
            "EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return dateFormat.format(Date.from(instant));
    }
}
