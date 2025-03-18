package org.swe.cart.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.swe.cart.entities.Group;
import org.swe.cart.entities.User;


public interface GroupRepository extends JpaRepository<Group, Integer>{
    List<Group> findByMembers_User(User user);
}
