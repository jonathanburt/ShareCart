package org.swe.cart.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.swe.cart.embeddables.GroupMemberKey;
import org.swe.cart.entities.GroupMember;

@Repository
public interface GroupMemberRepository extends JpaRepository<GroupMember, GroupMemberKey>{
}
