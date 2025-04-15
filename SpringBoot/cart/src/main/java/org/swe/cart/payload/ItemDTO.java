package org.swe.cart.payload;

import org.swe.cart.entities.Group;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
//@NoArgsConstructor
public class ItemDTO {
    private Integer itemId;
    private String name;
    private String description;
    private String category;
    private Float price;
    private String createdAtFormatted;    
}