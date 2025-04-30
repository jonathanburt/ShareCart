package org.swe.cart.services;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.TimeZone;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.swe.cart.entities.Group;
import org.swe.cart.entities.ShopList;
import org.swe.cart.exceptions.GroupDoesNotExistException;
import org.swe.cart.exceptions.ListAlreadyAddedToGroupException;
import org.swe.cart.payload.ListItemDTO;
import org.swe.cart.payload.ShopListDTO;
import org.swe.cart.repositories.GroupRepository;
import org.swe.cart.repositories.ListRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ListService {
    private final GroupRepository groupRepository;
    private final ListRepository listRepository;

    /**
     * adds a list to a given group
     * @author Jeremy Bullis jab525@case.edu
     * @param groupId the ID of the group to which the list is being added, found in the URL
     * @param name the name of the list
     * @return the data transfer object corresponding to the newly-created list
     * @throws GroupDoesNotExistException
     * @throws ListAlreadyAddedToGroupException
     */
    public ShopListDTO addListToGroup(Integer groupId, String name) throws GroupDoesNotExistException, ListAlreadyAddedToGroupException{
        Optional<Group> optionalGroup = groupRepository.findById(groupId);
        if(optionalGroup.isEmpty()) throw new GroupDoesNotExistException("Group does not exist");
        
        Group group = optionalGroup.get();

        if(listRepository.existsByGroupAndName(group, name)) throw new ListAlreadyAddedToGroupException("List already added to group");

        ShopList list = new ShopList();
        list.setGroup(group);
        list.setName(name);
        list.setItems(new HashSet<>());

        listRepository.save(list);

        return shopListToShopListDTO(list); 
    }

    /**
     * remove a list from a group
     * @author Jeremy Bullis jab525@case.edu
     * @param listId the ID of the list being removed, found in the URL
     * @return message verifying that the list has been deleted
     */
    public String deleteList(Integer listId){
        listRepository.deleteById(listId);
        return "List deleted";
    }

    /**
     * update a given list's information
     * @author Jeremy Bullis jab525@case.edu
     * @param listId the ID of the list being updated, found in the URL
     * @param name the new name of the list
     * @param groupId the ID of the new group to which the list belongs
     * @return the data transfer object corresponding to the list
     */
    public ShopListDTO updateList(Integer listId, String name, Integer groupId){
        ShopList list = listRepository.findById(listId).orElseThrow();
        Group group = groupRepository.findById(groupId).orElseThrow();
        list.setName(name);
        list.setGroup(group);
        list = listRepository.save(list);
        ShopListDTO shopListDTO = shopListToShopListDTO(list);
        return shopListDTO;
    }

    /**
     * return all lists corresponding to a given group
     * @author Jeremy Bullis jab525@case.edu
     * @param groupId the ID of the group from which the user is retrieving the lists, found in the URL
     * @return list of data transfer object corresponding to all the lists in the group
     */
    public List<ShopListDTO> getAllLists(Integer groupId){
        Group group = groupRepository.findById(groupId).orElseThrow();
        List<ShopList> lists = listRepository.findAllByGroup(group);

        return lists.stream()
        .map(list -> shopListToShopListDTO(list)).collect(Collectors.toList());
    }

    /**
     * returns a list with a given list ID
     * @author Jeremy Bullis jab525@case.edu
     * @param listId the ID of the list the method returns, found in the URL
     * @return the data transfer object corresponding to the given list
     */
    public ShopListDTO getList(Integer listId){
        ShopList list = listRepository.findById(listId).orElseThrow();
        return shopListToShopListDTO(list);
    }

    /**
     * convert a given list to its corresponding data transfer object
     * @author Jeremy Bullis jab525@case.edu
     * @param list the list that will be converted to a data transfer object
     * @return the data transfer object corresponding to the given list
     */
    private ShopListDTO shopListToShopListDTO(ShopList list){
        return new ShopListDTO(list.getName(), list.getId(),list.getGroup().getId(),formatInstantToHTTP(list.getCreatedAt()),
                    list.getItems().stream().map(
                        listItem -> new ListItemDTO(
                            listItem.getId().getItemid(), 
                            list.getId(), 
                            listItem.getId().getUserid(), 
                            listItem.getCommunal(), 
                            listItem.getQuantity(), 
                            listItem.getBought(),
                            formatInstantToHTTP(listItem.getCreatedAt()))
                    ).collect(Collectors.toList()));
    }

    /**
     * formats a given time to HTTP
     * @author Jeremy Bullis jab525@case.edu
     * @param instant the time which will be converted to HTTP
     * @return the correctly HTTP-formatted time
     */
    private String formatInstantToHTTP(Instant instant) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
            "EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return dateFormat.format(Date.from(instant));
    }
}
