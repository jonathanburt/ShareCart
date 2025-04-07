package org.swe.cart.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.swe.cart.embeddables.GroupInviteKey;
import org.swe.cart.entities.Group;
import org.swe.cart.entities.GroupInvite;
import org.swe.cart.entities.User;

@Repository
public interface GroupInviteRepository extends JpaRepository<GroupInvite, GroupInviteKey>{
    Optional<GroupInvite> findByUserAndGroup(User user, Group group);
    GroupInviteKey deleteByUserAndGroup(User user, Group group);
    Boolean existsByUserAndGroup(User user, Group group);
    List<GroupInvite> findAllByUser(User user);
}
