package org.swe.cart.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupInviteDTO1 {
    private String username;
    private Integer userId;
    private String invitedAtFormatted;
}
