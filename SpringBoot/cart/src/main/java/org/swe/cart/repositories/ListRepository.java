package org.swe.cart.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.swe.cart.entities.Group;
import org.swe.cart.entities.ShopList;

public interface ListRepository extends JpaRepository<ShopList, Integer> {
    public boolean existsByGroupAndName(Group group, String name);
}
