package org.swe.cart;

import org.springframework.data.jpa.repository.JpaRepository;
import org.swe.cart.entities.User;

//Spring will automatically generate this file

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);
}
