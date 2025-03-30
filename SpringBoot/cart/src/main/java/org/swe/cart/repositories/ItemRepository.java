package org.swe.cart.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.swe.cart.entities.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer>{ 
    Optional<Item> findById(Integer id);
}
