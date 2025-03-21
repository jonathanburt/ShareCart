package org.swe.cart.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.swe.cart.entities.Group;
import org.swe.cart.entities.ShopList;
import org.swe.cart.payload.GroupCreateDTO;
import org.swe.cart.payload.InviteUserDTO;
import org.swe.cart.payload.ListCreateDTO;
import org.swe.cart.services.GroupService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/group")
public class GroupController {
    
    private final GroupService groupService;

    @GetMapping("/getAllGroups")
    public ResponseEntity<List<Group>> getAllGroups() {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println(username);
        List<Group> groups = groupService.getUserGroups(username);
        return  ResponseEntity.ok(groups);
        //TODO change more userful return type
    }

    @PostMapping("/create")
    public ResponseEntity<Group> createGroup(@RequestBody GroupCreateDTO groupCreateDTO) {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Group newGroup = groupService.createGroup(username, groupCreateDTO);

        if(newGroup == null) return new ResponseEntity<>(null, HttpStatus.CONFLICT); //Group already esits with this name

        return ResponseEntity.status(HttpStatus.CREATED).body(newGroup);
        //TODO Change this to a more useable return type
    }

    @PostMapping("/{groupId}/invite")
    @PreAuthorize("hasAuthority('ROLE_ADMIN_GROUP_' + #groupId) or hasAuthority('ROLE_SHOPPER_GROUP_' + #groupId)")
    public ResponseEntity<String> inviteUsertoGroup(@PathVariable Integer groupId,
                                                    @RequestBody InviteUserDTO inviteUserDTO) {
        String respose = groupService.inviteUser(groupId, inviteUserDTO.getUsername());
        return ResponseEntity.ok(respose); //TODO Make useful return type
    }
    

    @PostMapping("/{groupId}/list/add")
    @PreAuthorize("hasAuthority('ROLE_ADMIN_GROUP_' + #groupId) or hasAuthority('ROLE_SHOPPER_GROUP_' + #groupId)")
    public ResponseEntity<ShopList> addListToGroup(@PathVariable Integer groupId,
                                                    @RequestBody ListCreateDTO listCreateDTO) {
        
        return groupService.addListToGroup(groupId, listCreateDTO.getName());
    }
    

    

}
