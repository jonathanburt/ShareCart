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

    /**
     * this method performs actions only on the network layer, and calls ListService for the rest
     * @author Jeremy Bullis jab525@case.edu
     * @param groupId the ID of the group from which the user is retrieving the lists, found in the URL
     * @return formatted response entity that either contains a bad request if the group doesn't exist, or that contains the list of lists returned by the getAllLists method of ListService
     */
    @GetMapping("/getall")
    @PreAuthorize("hasAuthority('ROLE_ADMIN_GROUP_' + #groupId) or hasAuthority('ROLE_SHOPPER_GROUP_' + #groupId) or hasAuthority('ROLE_MEMBER_GROUP_' + #groupId)")
    public ResponseEntity<List<ShopListDTO>> getAllLists(@PathVariable Integer groupId) {
        try {
            return ResponseEntity.ok(listService.getAllLists(groupId));
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * this method performs actions only on the network layer, and calls ListService for the rest
     * @author Jeremy Bullis jab525@case.edu
     * @param groupId the ID of the group from which the list is being retrieved, found in the URL
     * @param listId the ID of the list that the user is attempting to retrieve, found in the URL
     * @return formatted HTTP response containing either a bda request or containing the return of the getList method of ListService
     */
    @GetMapping("/{listId}/get")
    @PreAuthorize("hasAuthority('ROLE_ADMIN_GROUP_' + #groupId) or hasAuthority('ROLE_SHOPPER_GROUP_' + #groupId) or hasAuthority('ROLE_MEMBER_GROUP_' + #groupId)")
    public ResponseEntity<ShopListDTO> getList(@PathVariable Integer groupId, @PathVariable Integer listId) {
        try {
            return ResponseEntity.ok(listService.getList(listId));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    
    /**
     * this method performs actions only on the network layer, and calls ListService for the rest
     * @author Jeremy Bullis jab525@case.edu
     * @param groupId the ID of the group to which the list is being added, found in the URL
     * @param listCreateDTO data transfer object, containing the name of the list being created
     * @return formatted HTTP response containing a bad request or a conflict if either the group does not exist or the item is already in the group, or containing the result of the addListToGroup method of ListService
     * @throws GroupDoesNotExistException
     * @throws ListAlreadyAddedToGroupException
     */
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

    /**
     * this method performs actions only on the network layer, and calls ListService for the rest
     * @author Jeremy Bullis jab525@case.edu
     * @param groupId the ID of the group from which the list is being deleted, found in the URL
     * @param listId the ID of the list being deleted, found in the URL
     * @return the return of the deleteList method of ListService, which is a message verifying that the list has been deleted
     */
    @DeleteMapping("/{listId}/delete")
    @PreAuthorize("hasAuthority('ROLE_ADMIN_GROUP_' + #groupId) or hasAuthority('ROLE_SHOPPER_GROUP_' + #groupId)")
    public String deleteList(@PathVariable Integer groupId, @PathVariable Integer listId) {
        return listService.deleteList(listId);
    }

    /**
     * this method performs actions only on the network layer, and calls ListService for the rest
     * @author Jeremy Bullis jab525@case.edu
     * @param listId the ID of the list being updated, found in the URL
     * @param groupId the ID of the group to which the list belongs, found in the URL
     * @param updateListDTO data transfer object containing the name of the list
     * @return formatted HTTP response containing a data transfer object corresponding to the list being updated
     */
    @PutMapping("/{listId}/update")
    @PreAuthorize("hasAuthority('ROLE_ADMIN_GROUP_' + #groupId) or hasAuthority('ROLE_SHOPPER_GROUP_' + #groupId)")
    public ResponseEntity<ShopListDTO> updateList(@PathVariable Integer listId, @PathVariable Integer groupId, @RequestBody UpdateListDTO updateListDTO){
        String name = updateListDTO.getName();
        ShopListDTO shopListDTO = listService.updateList(listId, name, groupId);
        return ResponseEntity.status(HttpStatus.CREATED).body(shopListDTO);
    }
}
