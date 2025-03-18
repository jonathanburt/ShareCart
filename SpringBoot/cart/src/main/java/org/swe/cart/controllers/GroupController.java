package org.swe.cart.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.swe.cart.entities.Group;
import org.swe.cart.payload.GroupCreateDTO;
import org.swe.cart.security.CustomUserDetails;
import org.swe.cart.services.GroupService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/group")
public class GroupController {
    
    private final GroupService groupService;

    @GetMapping("/getAllGroups")
    public ResponseEntity<List<Group>> getAllGroups(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<Group> groups = groupService.getUserGroups(userDetails.getUsername());
        return  ResponseEntity.ok(groups);
    }

    @PostMapping("/create")
    public ResponseEntity<Group> createGroup(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody GroupCreateDTO groupCreateDTO) {
        String username = customUserDetails.getUsername();

        Group newGroup = groupService.createGroup(username, groupCreateDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(newGroup);
        //TODO Change this to a more useable return type

    }
    


    

}
