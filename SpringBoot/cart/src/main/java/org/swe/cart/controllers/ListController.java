package org.swe.cart.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.swe.cart.entities.ShopList;
import org.swe.cart.payload.ListCreateDTO;
import org.swe.cart.services.ListService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(path="/api/group/{groupId}/list")
@RequiredArgsConstructor
public class ListController {
    private final ListService listService;

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ROLE_ADMIN_GROUP_' + #groupId) or hasAuthority('ROLE_SHOPPER_GROUP_' + #groupId)")
    public ResponseEntity<ShopList> addListToGroup(@PathVariable Integer groupId,
                                                    @RequestBody ListCreateDTO listCreateDTO) {
        
        return listService.addListToGroup(groupId, listCreateDTO.getName());
    }

    @DeleteMapping("/{listId}/delete")
    public String deleteList(@PathVariable Integer groupId, @PathVariable Integer listId, @RequestBody String entity) {
        //TODO: process DELETE request
        
        return entity;
    }
}
