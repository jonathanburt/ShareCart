package org.swe.cart.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BuyItemDTO {
    private Integer itemId;
    private Integer listId;
    private Integer userId;
    private Boolean communal;
    private Integer quantity;
    private Boolean bought;
    private String createdAt;
}
