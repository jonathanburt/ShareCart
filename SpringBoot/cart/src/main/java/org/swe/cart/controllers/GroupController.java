package org.swe.cart.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.swe.cart.repositories.GroupRepository;
import org.swe.cart.repositories.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/group")
public class GroupController {
    
    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/create") //Add authorization check
    public ResponseEntity<?> createGroup(@RequestBody String groupName){
        //TODO
        //Get username from the JWT
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/getAllGroups")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> getAllGroups(@RequestParam String param) {
        //TODO
        return ResponseEntity.ok("Success");
    }
    

}
