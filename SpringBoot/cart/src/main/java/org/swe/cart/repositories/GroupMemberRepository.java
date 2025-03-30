package org.swe.cart.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.swe.cart.embeddables.GroupMemberKey;
import org.swe.cart.entities.Group;
import org.swe.cart.entities.GroupMember;
import org.swe.cart.entities.User;

@Repository
public interface GroupMemberRepository extends JpaRepository<GroupMember, GroupMemberKey>{
    Optional<GroupMember> findByUserAndGroup(User user, Group group);
    
}
