package org.swe.cart.services;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.swe.cart.embeddables.GroupInviteKey;
import org.swe.cart.embeddables.GroupMemberKey;
import org.swe.cart.entities.Group;
import org.swe.cart.entities.GroupInvite;
import org.swe.cart.entities.GroupMember;
import org.swe.cart.entities.Role;
import org.swe.cart.entities.ShopList;
import org.swe.cart.entities.User;
import org.swe.cart.payload.GroupCreateDTO;
import org.swe.cart.repositories.GroupInviteRepository;
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
    private final GroupInviteRepository groupInviteRepository;

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

    public String inviteUser(Integer groupId, String username){ //TODO Change to useful return type
        Optional<Group> optionalGroup = groupRepository.findById(groupId);
        if(optionalGroup.isEmpty()) return "Error: Group not found";
        Group group = optionalGroup.get();

        Optional<User> optionalUser = userRepository.findByUsername(username);
        if(optionalUser.isEmpty()) return "Error: User not found";
        User user = optionalUser.get();

        if(groupMemberRepository.findByUserAndGroup(user, group).isPresent()) return "User is already in group";

        if(groupInviteRepository.findByUserAndGroup(user, group).isPresent()) return "User has already been invited to group"; //TODO figure out how ot handle expired invites

        GroupInvite invite = new GroupInvite();
        invite.setUser(user);
        invite.setGroup(group);
        GroupInviteKey key = new GroupInviteKey(user.getId(),group.getId());
        invite.setId(key);
        groupInviteRepository.save(invite);

        return "User invited to group";
    }
}
