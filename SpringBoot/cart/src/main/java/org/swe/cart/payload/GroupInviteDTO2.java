package org.swe.cart.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GroupInviteDTO2 {
    private Integer groupId;
    private String groupName;
    private String invitedAt;
}
