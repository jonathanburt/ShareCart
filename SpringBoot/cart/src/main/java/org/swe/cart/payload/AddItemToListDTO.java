package org.swe.cart.payload;

import lombok.Data;

@Data
public class AddItemToListDTO {
    Integer itemId;
    Integer quantity;
    Boolean communal;
    Boolean bought;
}
