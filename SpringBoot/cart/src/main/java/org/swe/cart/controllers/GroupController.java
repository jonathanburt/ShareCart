package org.swe.cart.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.swe.cart.entities.Group;
import org.swe.cart.payload.ChangePermissionDTO;
import org.swe.cart.payload.GroupCreateDTO;
import org.swe.cart.payload.InviteUserDTO;
import org.swe.cart.services.GroupService;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping(path = "/api/group")
@RequiredArgsConstructor
public class GroupController {
    
    private final GroupService groupService;

    @GetMapping("/getAllGroups")
    public ResponseEntity<List<Group>> getAllGroups() {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Group> groups = groupService.getUserGroups(username);
        return  ResponseEntity.ok(groups);
        //TODO change more userful return type
    }

    @PostMapping("/create")
    public ResponseEntity<Group> createGroup(@RequestBody GroupCreateDTO groupCreateDTO) {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Group newGroup = groupService.createGroup(username, groupCreateDTO);

        if(newGroup == null) return new ResponseEntity<>(null, HttpStatus.CONFLICT); //Group already exists with this name

        return ResponseEntity.status(HttpStatus.CREATED).body(newGroup);
        //TODO Change this to a more useable return type
    }

    @PutMapping("/{groupId}/invite")
    @PreAuthorize("hasAuthority('ROLE_ADMIN_GROUP_' + #groupId) or hasAuthority('ROLE_SHOPPER_GROUP_' + #groupId)")
    public ResponseEntity<String> inviteUsertoGroup(@PathVariable Integer groupId,
                                                    @RequestBody InviteUserDTO inviteUserDTO) {
        String response = groupService.inviteUser(groupId, inviteUserDTO.getUsername());
        return ResponseEntity.ok(response); //TODO Make useful return type
    }

    @PutMapping("/{groupId}/invite/accept")
    public String acceptInvite(@PathVariable Integer groupId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String response = groupService.acceptInvite(groupId, auth);
        return response; //TODO change to ResponseEntity
    }

    @PutMapping("/{groupId}/invite/decline")
    public String declineInvite(@PathVariable Integer groupId) {
       Authentication auth = SecurityContextHolder.getContext().getAuthentication();
       String response = groupService.declineInvite(groupId, auth);
        return response; //TODO change to ResponseEntity
    }

    @PutMapping("/{groupId}/users/{userId}/remove")
    @PreAuthorize("hasAuthority('ROLE_ADMIN_GROUP_' + #groupId)")
    public String removeUser(@PathVariable Integer groupId, @PathVariable Integer userId) {
        String response = groupService.removeUser(groupId, userId);
        return response; //TODO change to ResponseEntity
    }

    @PutMapping("/{groupId}/users/{userId}/permissions")
    @PreAuthorize("hasAuthority('ROLE_GROUP_ADMIN_' + #groupId)")
    public String changeUserPermissions(@PathVariable Integer groupId, @PathVariable Integer userId,
                                        @RequestBody ChangePermissionDTO changePermissionDTO) {
        String response = groupService.changeUserPermission(groupId, userId, changePermissionDTO.getRole());
        
        return response; //TODO change to ResponseEntity
    }
    
    @DeleteMapping("/{groupId}/delete")
    @PreAuthorize("hasAuthority('ROLE_GROUP_ADMIN_' + #groupId)")
    public String deleteGroup(@PathVariable Integer groupId) {
        String response = groupService.deleteGroup(groupId);
        
        return response; //TODO change to ResponseEntity
    }
    
}
