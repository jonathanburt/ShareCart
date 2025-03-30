package org.swe.cart.services;

import java.util.HashSet;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.swe.cart.entities.Group;
import org.swe.cart.entities.ShopList;
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

}
