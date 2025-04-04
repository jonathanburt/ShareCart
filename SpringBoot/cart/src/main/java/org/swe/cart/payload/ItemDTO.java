package org.swe.cart.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDTO {
    private String name;
    private Integer groupId;
    private String createdAtFormatted;
    private String category;
    private String description;
    private Integer price;
    private Integer itemId;
}