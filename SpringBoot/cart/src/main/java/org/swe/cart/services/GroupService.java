package org.swe.cart.services;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.swe.cart.embeddables.GroupInviteKey;
import org.swe.cart.embeddables.GroupMemberKey;
import org.swe.cart.entities.Group;
import org.swe.cart.entities.GroupInvite;
import org.swe.cart.entities.GroupMember;
import org.swe.cart.entities.Role;
import org.swe.cart.entities.User;
import org.swe.cart.payload.GroupCreateDTO;
import org.swe.cart.repositories.GroupInviteRepository;
import org.swe.cart.repositories.GroupMemberRepository;
import org.swe.cart.repositories.GroupRepository;
import org.swe.cart.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final GroupInviteRepository groupInviteRepository;

    public List<Group> getUserGroups(String username){
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found" + username));

        return groupRepository.findByMembers_User(user);
    }

    public Group createGroup(String username, GroupCreateDTO groupCreateDTO){
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if(groupRepository.existsByName(groupCreateDTO.getName())) return null;
        Group group = new Group();
        group.setName(groupCreateDTO.getName());
        group = groupRepository.save(group);

        GroupMember creator = new GroupMember();
        creator.setUser(user);
        creator.setGroup(group);
        creator.setRole(Role.ADMIN);

        GroupMemberKey creatorKey = new GroupMemberKey(user.getId(),group.getId());
        creator.setId(creatorKey);

        groupMemberRepository.save(creator);

        return groupRepository.findById(group.getId()).orElseThrow();
    }

    public String inviteUser(Integer groupId, String username){ //TODO Change to useful return type
        Optional<Group> optionalGroup = groupRepository.findById(groupId);
        if(optionalGroup.isEmpty()) return "Error: Group not found";
        Group group = optionalGroup.get();

        Optional<User> optionalUser = userRepository.findByUsername(username);
        if(optionalUser.isEmpty()) return "Error: User not found";
        User user = optionalUser.get();

        if(groupMemberRepository.findByUserAndGroup(user, group).isPresent()) return "User is already in group";

        if(groupInviteRepository.findByUserAndGroup(user, group).isPresent()) return "User has already been invited to group"; //TODO figure out how ot handle expired invites

        GroupInvite invite = new GroupInvite();
        invite.setUser(user);
        invite.setGroup(group);
        GroupInviteKey key = new GroupInviteKey(user.getId(),group.getId());
        invite.setId(key);
        groupInviteRepository.save(invite);

        return "User invited to group";
    }

    public String acceptInvite(Integer groupId, Authentication auth){
        String username = auth.getName();
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User could not be found"));

        Optional<Group> optionalGroup = groupRepository.findById(groupId);
        if(optionalGroup.isEmpty()) return "Group not found";
        Group group = optionalGroup.get();

        Optional<GroupInvite> optionalGroupInvite = groupInviteRepository.findByUserAndGroup(user, group);
        if(optionalGroupInvite.isEmpty()) return username + " has not been invited to this group";

        Optional<GroupMember> optionalGroupMember = groupMemberRepository.findByUserAndGroup(user, group);
        if(optionalGroupMember.isPresent()) return username + " is already a member of this group";

        GroupMember newMember = new GroupMember();
        newMember.setGroup(group);
        newMember.setUser(user);
        newMember.setRole(Role.MEMBER);
        GroupMemberKey key = new GroupMemberKey();
        key.setGroupid(group.getId());
        key.setUserid(user.getId());
        newMember.setId(key);

        groupMemberRepository.save(newMember);
        groupInviteRepository.deleteByUserAndGroup(user, group);

        return username + " has been added to group as a member";
    }

    public String declineInvite(Integer groupId, Authentication auth){
        String username = auth.getName();
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User could not be found"));

        Optional<Group> optionalGroup = groupRepository.findById(groupId);
        if(optionalGroup.isEmpty()) return "Group not found";
        Group group = optionalGroup.get();

        Optional<GroupInvite> optionalGroupInvite = groupInviteRepository.findByUserAndGroup(user, group);
        if(optionalGroupInvite.isEmpty()) return username + " has not been invited to this group";

        Optional<GroupMember> optionalGroupMember = groupMemberRepository.findByUserAndGroup(user, group);
        if(optionalGroupMember.isPresent()) return username + " is already a member of this group";

        groupInviteRepository.deleteByUserAndGroup(user, group);

        return "Group Invite decline";
    }

    public String removeUser(Integer groupId, Integer userId){
        User user = userRepository.findById(userId).orElseThrow();
        Group group = groupRepository.findById(groupId).orElseThrow();
        GroupMember member = groupMemberRepository.findByUserAndGroup(user, group).orElseThrow();

        groupMemberRepository.deleteById(member.getId());

        return "User removed from group";
    }

    public String changeUserPermission(Integer groupId, Integer userId, Role role){
        User user = userRepository.findById(userId).orElseThrow();
        Group group = groupRepository.findById(userId).orElseThrow();
        GroupMember member = groupMemberRepository.findByUserAndGroup(user, group).orElseThrow();

        member.setRole(role);

        groupMemberRepository.save(member);

        return "User permissions changed to " + role.name();
    }

    public String deleteGroup(Integer groupId){
        groupRepository.deleteById(groupId);
        return "Group removed";
    }

}
