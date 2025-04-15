package org.swe.cart.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.swe.cart.exceptions.GroupDoesNotExistException;
import org.swe.cart.exceptions.ListAlreadyAddedToGroupException;
import org.swe.cart.payload.ListCreateDTO;
import org.swe.cart.payload.ShopListDTO;
import org.swe.cart.payload.UpdateListDTO;
import org.swe.cart.services.ListService;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping(path="/api/group/{groupId}/list")
@RequiredArgsConstructor
public class ListController {
    private final ListService listService;

    @GetMapping("/getall")
    @PreAuthorize("hasAuthority('ROLE_ADMIN_GROUP_' + #groupId) or hasAuthority('ROLE_SHOPPER_GROUP_' + #groupId) or hasAuthority('ROLE_MEMBER_GROUP_' + #groupId)")
    public ResponseEntity<List<ShopListDTO>> getAllLists(@PathVariable Integer groupId) {
        try {
            return ResponseEntity.ok(listService.getAllLists(groupId));
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{listId}/get")
    @PreAuthorize("hasAuthority('ROLE_ADMIN_GROUP_' + #groupId) or hasAuthority('ROLE_SHOPPER_GROUP_' + #groupId) or hasAuthority('ROLE_MEMBER_GROUP_' + #groupId)")
    public ResponseEntity<ShopListDTO> getList(@PathVariable Integer groupId, @PathVariable Integer listId) {
        try {
            return ResponseEntity.ok(listService.getList(listId));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    
    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ROLE_ADMIN_GROUP_' + #groupId) or hasAuthority('ROLE_SHOPPER_GROUP_' + #groupId)")
    public ResponseEntity<ShopListDTO> addListToGroup(@PathVariable Integer groupId,
                                                    @RequestBody ListCreateDTO listCreateDTO) throws GroupDoesNotExistException, ListAlreadyAddedToGroupException {
        ShopListDTO shopListDTO = null;
        try{
            shopListDTO = listService.addListToGroup(groupId, listCreateDTO.getName());
        } catch (GroupDoesNotExistException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(shopListDTO);
        } catch (ListAlreadyAddedToGroupException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(shopListDTO);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(shopListDTO);
    }

    @DeleteMapping("/{listId}/delete")
    @PreAuthorize("hasAuthority('ROLE_ADMIN_GROUP_' + #groupId) or hasAuthority('ROLE_SHOPPER_GROUP_' + #groupId)")
    public String deleteList(@PathVariable Integer groupId, @PathVariable Integer listId) {
        return listService.deleteList(listId);
    }

    @PutMapping("/{listId}/update")
    @PreAuthorize("hasAuthority('ROLE_ADMIN_GROUP_' + #groupId) or hasAuthority('ROLE_SHOPPER_GROUP_' + #groupId)")
    public ResponseEntity<ShopListDTO> updateList(@PathVariable Integer listId, @PathVariable Integer groupId, @RequestBody UpdateListDTO updateListDTO){
        String name = updateListDTO.getName();
        ShopListDTO shopListDTO = listService.updateList(listId, name, groupId);
        return ResponseEntity.status(HttpStatus.CREATED).body(shopListDTO);
    }
}
