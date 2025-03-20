package org.swe.cart.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.swe.cart.embeddables.InviteUserKey;
import org.swe.cart.entities.GroupInvite;

@Repository
public interface GroupInviteRepository extends JpaRepository<GroupInvite, InviteUserKey>{
    
}
