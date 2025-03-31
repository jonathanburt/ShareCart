package org.swe.cart.payload;

import java.time.Instant;

import org.swe.cart.entities.GroupRole;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupMemberDTO {
    private String username;
    private Integer userId;
    private GroupRole role;
    private Instant joinedAt;
}
