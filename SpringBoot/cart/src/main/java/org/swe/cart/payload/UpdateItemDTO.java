package org.swe.cart.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateItemDTO {
    private String name;
    private String description;
    private String category;
    private Float price;
}
