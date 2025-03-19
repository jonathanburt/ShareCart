package org.swe.cart.services;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.swe.cart.embeddables.GroupMemberKey;
import org.swe.cart.entities.Group;
import org.swe.cart.entities.GroupMember;
import org.swe.cart.entities.Role;
import org.swe.cart.entities.ShopList;
import org.swe.cart.entities.User;
import org.swe.cart.payload.GroupCreateDTO;
import org.swe.cart.repositories.GroupMemberRepository;
import org.swe.cart.repositories.GroupRepository;
import org.swe.cart.repositories.ListRepository;
import org.swe.cart.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final ListRepository listRepository;

    public List<Group> getUserGroups(String username){
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found" + username));

        return groupRepository.findByMembers_User(user);
    }

    public Group createGroup(String username, GroupCreateDTO groupCreateDTO){
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if(groupRepository.existsByName(groupCreateDTO.getName())) return null;
        Group group = new Group();
        group.setName(groupCreateDTO.getName());
        groupRepository.save(group);

        GroupMember creator = new GroupMember();
        creator.setUser(user);
        creator.setGroup(group);
        creator.setRole(Role.ADMIN);

        GroupMemberKey creatorKey = new GroupMemberKey(user.getId(),group.getId());
        creator.setId(creatorKey);

        groupMemberRepository.save(creator);

        return group;
    }

    public boolean canUserManageLists(String username, Integer groupId){
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if(optionalUser.isEmpty()){
            return false;
        }
        User user = optionalUser.get();
        Optional<Group> optionalGroup = groupRepository.findById(groupId);
        if (optionalGroup.isEmpty()) {
            return false;
        }
        Group group = optionalGroup.get();
        Optional<GroupMember> optionalGroupMember = groupMemberRepository.findByUserAndGroup(user, group);
        if (optionalGroupMember.isEmpty()) {
            return false;
        }

        GroupMember groupMember = optionalGroupMember.get();
        return (groupMember.getRole() == Role.ADMIN || groupMember.getRole() == Role.SHOPPER);
    }

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

        return new ResponseEntity<>(list, HttpStatus.CREATED); 
    }
}
