package org.swe.cart.payload;

import lombok.Data;

@Data
public class ItemCreateDTO {
    private String name;
    private String description;
    private String category;
    private Float price;
}