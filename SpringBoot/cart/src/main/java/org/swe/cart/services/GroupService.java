package org.swe.cart.services;

import java.util.List;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.swe.cart.entities.Group;
import org.swe.cart.entities.User;
import org.swe.cart.repositories.GroupRepository;
import org.swe.cart.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GroupService {
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;

    public List<Group> getUserGroups(String username){
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found" + username));

        return groupRepository.findByMembers_User(user);
    }
}
