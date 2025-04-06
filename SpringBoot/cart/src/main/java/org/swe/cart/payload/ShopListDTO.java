package org.swe.cart.payload;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShopListDTO {
    private String name;
    private Integer listId;
    private Integer groupId;
    private String createdAt;
    private List<ListItemDTO> items;
}
