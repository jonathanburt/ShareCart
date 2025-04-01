package org.swe.cart.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.swe.cart.entities.Group;
import org.swe.cart.entities.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer>{ 
    Optional<Item> findById(Integer id);
    List<Item> findByGroup(Group group);
    Optional<Item> findByIdAndGroup(Integer id, Group group);
}
