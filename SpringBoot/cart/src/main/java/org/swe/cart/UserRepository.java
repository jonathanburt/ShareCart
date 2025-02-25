package org.swe.cart;

import org.springframework.data.repository.CrudRepository;
import org.swe.cart.entities.User;

//Spring will automatically generate this file

public interface UserRepository extends CrudRepository<User, Integer> {
    
}
