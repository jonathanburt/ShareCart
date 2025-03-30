package org.swe.cart.payload;

import org.swe.cart.entities.Role;

import lombok.Data;

@Data
public class ChangePermissionDTO {
    private Role role;
}
