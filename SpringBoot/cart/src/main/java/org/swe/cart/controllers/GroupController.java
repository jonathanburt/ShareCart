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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.swe.cart.exceptions.UserAlreadyInGroupException;
import org.swe.cart.exceptions.UserNotInvitedToGroupException;
import org.swe.cart.payload.ChangePermissionDTO;
import org.swe.cart.payload.GroupCreateDTO;
import org.swe.cart.payload.GroupDTO;
import org.swe.cart.payload.InviteUserDTO;
import org.swe.cart.services.GroupService;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping(path = "/api/group")
@RequiredArgsConstructor
public class GroupController {
    
    private final GroupService groupService;

    @GetMapping("/get/all")
    public ResponseEntity<List<GroupDTO>> getAllGroups() {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<GroupDTO> groups = groupService.getUserGroups(username);
        return  ResponseEntity.ok(groups);
        //TODO change more userful return type
    }

    @GetMapping("/get/{groupId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN_GROUP_' + #groupId) or hasAuthority('ROLE_SHOPPER_GROUP_' + #groupId) or hasAuthority('ROLE_MEMBER_GROUP_' + #groupId)")
    public ResponseEntity<GroupDTO> getGroubById(@PathVariable Integer groupId){
        return ResponseEntity.ok(groupService.getGroupById(groupId));
    }

    @PostMapping("/create")
    public ResponseEntity<GroupDTO> createGroup(@RequestBody GroupCreateDTO groupCreateDTO) {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        GroupDTO newGroup = groupService.createGroup(username, groupCreateDTO);

        if(newGroup == null) return new ResponseEntity<>(null, HttpStatus.CONFLICT); //Group already exists with this name

        return ResponseEntity.status(HttpStatus.CREATED).body(newGroup);
        //TODO Change this to a more useable return type
    }

    @PostMapping("/{groupId}/invite")
    @PreAuthorize("hasAuthority('ROLE_ADMIN_GROUP_' + #groupId) or hasAuthority('ROLE_SHOPPER_GROUP_' + #groupId)")
    public ResponseEntity<GroupDTO> inviteUsertoGroup(@PathVariable Integer groupId,
                                                    @RequestBody InviteUserDTO inviteUserDTO) {
        try {
            GroupDTO response = groupService.inviteUser(groupId, inviteUserDTO.getUsername());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/{groupId}/invite/accept")
    public ResponseEntity<GroupDTO> acceptInvite(@PathVariable Integer groupId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        try {
            GroupDTO response = groupService.acceptInvite(groupId, auth);
            return ResponseEntity.ok(response);
        } catch (UserAlreadyInGroupException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } catch (UserNotInvitedToGroupException e){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        
    }

    @PostMapping("/{groupId}/invite/decline")
    public ResponseEntity<GroupDTO> declineInvite(@PathVariable Integer groupId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        try {
            GroupDTO response = groupService.declineInvite(groupId, auth);
            return ResponseEntity.ok(response);
        } catch (UserAlreadyInGroupException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } catch (UserNotInvitedToGroupException e){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        
    }

    @PostMapping("/{groupId}/users/{userId}/remove")
    @PreAuthorize("hasAuthority('ROLE_ADMIN_GROUP_' + #groupId)")
    public ResponseEntity<GroupDTO> removeUser(@PathVariable Integer groupId, @PathVariable Integer userId) {
        GroupDTO response = groupService.removeUser(groupId, userId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{groupId}/users/{userId}/permissions")
    @PreAuthorize("hasAuthority('ROLE_GROUP_ADMIN_' + #groupId)")
    public ResponseEntity<GroupDTO> changeUserPermissions(@PathVariable Integer groupId, @PathVariable Integer userId,
                                        @RequestBody ChangePermissionDTO changePermissionDTO) {
        GroupDTO response = groupService.changeUserPermission(groupId, userId, changePermissionDTO.getRole());
        
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{groupId}/delete")
    @PreAuthorize("hasAuthority('ROLE_GROUP_ADMIN_' + #groupId)")
    public String deleteGroup(@PathVariable Integer groupId) {
        String response = groupService.deleteGroup(groupId);
        
        return response; //TODO change to ResponseEntity
    }
    
}
