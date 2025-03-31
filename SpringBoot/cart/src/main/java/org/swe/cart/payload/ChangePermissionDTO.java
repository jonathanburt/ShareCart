package org.swe.cart.payload;

import org.swe.cart.entities.GroupRole;

import lombok.Data;

@Data
public class ChangePermissionDTO {
    private GroupRole role;
}
