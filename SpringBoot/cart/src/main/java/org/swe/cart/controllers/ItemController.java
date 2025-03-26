package org.swe.cart.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping(path="/api/group/{groupId}/item")
public class ItemController {
    
    @GetMapping("/getall")
    public String getAllItems(@PathVariable Integer groupId, @RequestParam String param) {
        return new String();
    }
    
    @PostMapping("/add")
    public String addItem(@PathVariable Integer groupId, @RequestBody String entity) {
        //TODO: process POST request
        
        return entity;
    }

    @PutMapping("/{itemId}/update")
    public String updateItem(@PathVariable Integer itemId, @RequestBody String entity) {
        //TODO: process PUT request
        
        return entity;
    }
    
}
