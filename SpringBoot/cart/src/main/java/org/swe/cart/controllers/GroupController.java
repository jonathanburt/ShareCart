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

    /**
     * This method only directly performs actions at the network layer, and calls to the Group Service for the rest.
     * @author Jonah Lorenzo jbl113@case.edu
     * @return A formatted HTTP response containing information about all of a users groups
     */
    @GetMapping("/get/all")
    public ResponseEntity<List<GroupDTO>> getAllGroups() {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<GroupDTO> groups = groupService.getUserGroups(username);
        return  ResponseEntity.ok(groups);
    }

    /**
     * This method only directly performs actions at the network layer, and calls to the Group Service for the rest.
     * @author Jonah Lorenzo jbl113@case.edu
     * @param groupId The ID of the group the requester is attempting to access, provided in the URL
     * @return A formatted HTTP response with the group corresponding to groupId if the requester has authorization to access this group
     */
    @GetMapping("/get/{groupId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN_GROUP_' + #groupId) or hasAuthority('ROLE_SHOPPER_GROUP_' + #groupId) or hasAuthority('ROLE_MEMBER_GROUP_' + #groupId)")
    public ResponseEntity<GroupDTO> getGroubById(@PathVariable Integer groupId){
        return ResponseEntity.ok(groupService.getGroupById(groupId));
    }

    /**
     * This method only directly performs actions at the network layer, and calls to the Group Service for the rest.
     * @author Jonah Lorenzo jbl113@case.edu
     * @param groupCreateDTO This is a data transfer object representing a new group, holding a String for the new groups name
     * @return A formatted HTTP response with the details of the new group, or nothing if group creating failed.
     */
    @PostMapping("/create")
    public ResponseEntity<GroupDTO> createGroup(@RequestBody GroupCreateDTO groupCreateDTO) {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        GroupDTO newGroup = groupService.createGroup(username, groupCreateDTO);

        if(newGroup == null) return new ResponseEntity<>(null, HttpStatus.CONFLICT); //Group already exists with this name

        return ResponseEntity.status(HttpStatus.CREATED).body(newGroup);
    }

    /**
     * This method only directly performs actions at the network layer, and calls to the Group Service for the rest.
     * @author Jonah Lorenzo jbl113@case.edu
     * @param groupId The ID of the group the requester is attempting to create an invite for. Provided in the URL.
     * @param inviteUserDTO A data transfer object containing only a String username of the user the requester wants to invite to the group
     * @return A formatted HTTP response containg the updated group if successful, or returning nothing otherwise
     */
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

    /**
     * This method only directly performs actions at the network layer, and calls to the Group Service for the rest.
     * @author Jonah Lorenzo jbl113@case.edu
     * @param groupId The ID corresponding to the group the requester is accepting an invite to
     * @return A formatted HTTP response containing the details of the group the requester has accepted an invite to, or nothing if failure
     */
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

    /**
     * This method only directly performs actions at the network layer, and calls to the Group Service for the rest.
     * @author Jonah Lorenzo jbl113@case.edu
     * @param groupId The ID corresponding to the group the requester is declining an invite to
     * @return A formatted HTTP response containing a String if successful, or nothing if failing
     */
    @PostMapping("/{groupId}/invite/decline")
    public ResponseEntity<String> declineInvite(@PathVariable Integer groupId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        try {
            groupService.declineInvite(groupId, auth);
            return ResponseEntity.ok("Invitation Declined");
        } catch (UserAlreadyInGroupException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } catch (UserNotInvitedToGroupException e){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        
    }

    /**
     * This method only directly performs actions at the network layer, and calls to the Group Service for the rest.
     * @author Jonah Lorenzo jbl113@case.edu
     * @param groupId The ID corresponding to the group the requester is removing a user from
     * @param userId The ID corresponding to the user the requester is removing a user from
     * @return A formatted HTTP request containing the updated group details of the group after removal of the user
     */
    @PostMapping("/{groupId}/users/{userId}/remove")
    @PreAuthorize("hasAuthority('ROLE_ADMIN_GROUP_' + #groupId)")
    public ResponseEntity<GroupDTO> removeUser(@PathVariable Integer groupId, @PathVariable Integer userId) {
        GroupDTO response = groupService.removeUser(groupId, userId);
        return ResponseEntity.ok(response);
    }

    /**
     * This method only directly performs actions at the network layer, and calls to the Group Service for the rest.
     * @author Jonah Lorenzo jbl113@case.edu
     * @param groupId The ID corresponding to the group the user is leaving
     * @return
     */
    @PostMapping("/{groupId}/users/leave")
    public String leaveGroup(@PathVariable Integer groupId, @RequestBody String entity) {
        //TODO: process POST request
        
        return entity;
    }
    
    /**
     * This method only directly performs actions at the network layer, and calls to the Group Service for the rest.
     * @author Jonah Lorenzo jbl113@case.edu
     * @param groupId The ID corresponding to the group the requester is changing permissions in
     * @param userId The ID of the user the requester is changing the permissions of
     * @param changePermissionDTO A data transfer object containg the new desired role for the user
     * @return A formatted HTTP response with the updated group details
     */
    @PostMapping("/{groupId}/users/{userId}/permissions")
    @PreAuthorize("hasAuthority('ROLE_GROUP_ADMIN_' + #groupId)")
    public ResponseEntity<GroupDTO> changeUserPermissions(@PathVariable Integer groupId, @PathVariable Integer userId,
                                        @RequestBody ChangePermissionDTO changePermissionDTO) {
        GroupDTO response = groupService.changeUserPermission(groupId, userId, changePermissionDTO.getRole());
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * This method only directly performs actions at the network layer, and calls to the Group Service for the rest.
     * @author Jonah Lorenzo jbl113@case.edu
     * @param groupId The ID corresponding to the group the requester is attempting to remove
     * @return
     */
    @DeleteMapping("/{groupId}/delete")
    @PreAuthorize("hasAuthority('ROLE_GROUP_ADMIN_' + #groupId)")
    public String deleteGroup(@PathVariable Integer groupId) {
        String response = groupService.deleteGroup(groupId);
        
        return response; //TODO change to ResponseEntity
    }   
}
