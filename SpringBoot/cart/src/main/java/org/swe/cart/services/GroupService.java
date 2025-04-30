package org.swe.cart.services;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.swe.cart.embeddables.GroupInviteKey;
import org.swe.cart.embeddables.GroupMemberKey;
import org.swe.cart.entities.Group;
import org.swe.cart.entities.GroupInvite;
import org.swe.cart.entities.GroupMember;
import org.swe.cart.entities.GroupRole;
import org.swe.cart.entities.User;
import org.swe.cart.exceptions.UserAlreadyInGroupException;
import org.swe.cart.exceptions.UserAlreadyInvitedToGroupException;
import org.swe.cart.exceptions.UserNotInvitedToGroupException;
import org.swe.cart.payload.GroupCreateDTO;
import org.swe.cart.payload.GroupDTO;
import org.swe.cart.payload.GroupInviteDTO1;
import org.swe.cart.payload.GroupMemberDTO;
import org.swe.cart.repositories.GroupInviteRepository;
import org.swe.cart.repositories.GroupMemberRepository;
import org.swe.cart.repositories.GroupRepository;
import org.swe.cart.repositories.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final GroupInviteRepository groupInviteRepository;

    /**
     * Finds all groups the user corresponding to username is a member of.
     * @author Jonah Lorenzo jbl113@case.edu
     * @param username The username corresponding to the user that the method finds GroupMember objects for
     * @return A list of data transfer objects containg information on the groups the client is a member of: a String name, Integer ID, String creation timestamp, a lsit of GroupMemberDTOs, and a list of GroupInviteDTOs.
     */
    public List<GroupDTO> getUserGroups(String username) {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

    return groupRepository.findByMembers_User(user).stream()
        .map(group -> new GroupDTO(
            group.getName(),
            group.getId(),
            formatInstantToHTTP(group.getCreatedAt()),
            (List<GroupMemberDTO>) group.getMembers().stream()
                .map(member -> new GroupMemberDTO(
                    member.getUser().getUsername(),
                    member.getUser().getId(),
                    member.getRole(),
                    formatInstantToHTTP(member.getCreated_at())
                ))
                .collect(Collectors.toList()),
            (List<GroupInviteDTO1>) group.getInvites().stream()
                .map(invite -> new GroupInviteDTO1(
                    invite.getUser().getUsername(),
                    invite.getUser().getId(),
                    formatInstantToHTTP(invite.getCreated_at())
                ))
                .collect(Collectors.toList())
            ))
        .collect(Collectors.toList());
    }

    /**
     * Finds a single group from the provided ID
     * @author Jonah Lorenzo jbl113@case.edu
     * @param groupId The ID of the group to locate
     * @return A data transfer object containg details of the found group
     */
    public GroupDTO getGroupById(Integer groupId){
        Group group = groupRepository.findById(groupId).orElseThrow();
        GroupDTO groupDTO = groupToGroupDTO(group);

        return groupDTO;
    }

    /**
     * Creates a new Group in the DB
     * @author Jonah Lorenzo jbl113@case.edu
     * @param username The username corresponding to the user creating the group
     * @param groupCreateDTO A data transfer object containing a unique String name that will be assigned to the new group
     * @return A data transfer object containg details of the new group.
     */
    public GroupDTO createGroup(String username, GroupCreateDTO groupCreateDTO){
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if(groupRepository.existsByName(groupCreateDTO.getName())) return null;
        Group group = new Group();
        group.setName(groupCreateDTO.getName());
        Group savedGroup = groupRepository.save(group);

        GroupMember creator = new GroupMember();
        creator.setUser(user);
        creator.setGroup(savedGroup);
        creator.setRole(GroupRole.ADMIN);

        GroupMemberKey creatorKey = new GroupMemberKey(user.getId(),group.getId());
        creator.setId(creatorKey);
        creator = groupMemberRepository.save(creator);
        savedGroup.getMembers().add(creator);
        GroupDTO groupDTO = groupToGroupDTO(savedGroup);
        return groupDTO;
    }

    /**
     * Creates a new GroupInvite object in the DB
     * @author Jonah Lorenzo jbl113@case.edu
     * @param groupId The ID corresponding to the group the invite is created in.
     * @param username The username corresponding to the user to be invited to the group.
     * @return An updated data transfer object containg group details
     * @throws UserAlreadyInGroupException
     * @throws UserAlreadyInvitedToGroupException
     */
    public GroupDTO inviteUser(Integer groupId, String username) throws UserAlreadyInGroupException, UserAlreadyInvitedToGroupException{ //TODO Change to useful return type
        Group group = groupRepository.findById(groupId).orElseThrow();
        User user = userRepository.findByUsername(username).orElseThrow();

        if(groupMemberRepository.findByUserAndGroup(user, group).isPresent()) throw new UserAlreadyInGroupException(username);

        if(groupInviteRepository.findByUserAndGroup(user, group).isPresent()) throw new UserAlreadyInvitedToGroupException(username); //TODO figure out how ot handle expired invites

        GroupInvite invite = new GroupInvite();
        invite.setUser(user);
        invite.setGroup(group);
        GroupInviteKey key = new GroupInviteKey(user.getId(),group.getId());
        invite.setId(key);
        groupInviteRepository.save(invite);
        group.getInvites().add(invite);

        return groupToGroupDTO(groupRepository.findById(groupId).get());
    }

    /**
     * Removes an existing GroupInvite enrty from the DB and creates a new GroupMember entity.
     * @author Jonah Lorenzo jbl113@case.edu
     * @param groupId The ID corresponding to the group the invite exists in.
     * @param auth An Authentication object corresponding to the requester
     * @return An updated data transfer object for the Group.
     * @throws UserNotInvitedToGroupException
     * @throws UserAlreadyInGroupException
     */
    @Transactional
    public GroupDTO acceptInvite(Integer groupId, Authentication auth) throws UserNotInvitedToGroupException, UserAlreadyInGroupException{
        String username = auth.getName();
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User could not be found"));

        Group group = groupRepository.findById(groupId).orElseThrow();

        if(!groupInviteRepository.existsByUserAndGroup(user, group)) throw new UserNotInvitedToGroupException(username);

        if(groupMemberRepository.findByUserAndGroup(user, group).isPresent()) throw new UserAlreadyInGroupException(username);

        GroupMember newMember = new GroupMember();
        newMember.setGroup(group);
        newMember.setUser(user);
        newMember.setRole(GroupRole.MEMBER);
        GroupMemberKey key = new GroupMemberKey();
        key.setGroupid(group.getId());
        key.setUserid(user.getId());
        newMember.setId(key);

        groupMemberRepository.save(newMember);
        groupInviteRepository.deleteByUserAndGroup(user, group);

        return groupToGroupDTO(groupRepository.findById(groupId).get());
    }

    /**
     * Removes an existing GroupInvite entry from the DB.
     * @author Jonah Lorenzo jbl113@case.edu
     * @param groupId The ID corresponding to the group the invite exists for.
     * @param auth An Authentication object corresponding to the requester.
     * @throws UserNotInvitedToGroupException
     * @throws UserAlreadyInGroupException
     */
    @Transactional
    public void declineInvite(Integer groupId, Authentication auth) throws UserNotInvitedToGroupException, UserAlreadyInGroupException{
        String username = auth.getName();
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User could not be found"));

        Group group = groupRepository.findById(groupId).orElseThrow();

        if(!groupInviteRepository.existsByUserAndGroup(user, group)) throw new UserNotInvitedToGroupException(username);

        if(groupMemberRepository.findByUserAndGroup(user, group).isPresent()) throw new UserAlreadyInGroupException(username);

        groupInviteRepository.deleteByUserAndGroup(user, group);
    }

    /**
     * Deletes a GroupMember entity from the DB.
     * @author Jonah Lorenzo jbl113@case.edu
     * @param groupId The ID corresponding to the group the GroupMember entry is linked to.
     * @param userId The ID corresponding to to the user in the GroupMember entry.
     * @return An updated data transfer object with Group details.
     */
    public GroupDTO removeUser(Integer groupId, Integer userId){
        User user = userRepository.findById(userId).orElseThrow();
        Group group = groupRepository.findById(groupId).orElseThrow();
        GroupMember member = groupMemberRepository.findByUserAndGroup(user, group).orElseThrow();

        groupMemberRepository.deleteById(member.getId());

        return groupToGroupDTO(groupRepository.findById(userId).get());
    }

    /**
     * Changes the GroupRole in a GroupMember entry.
     * @author Jonah Lorenzo jbl113@case.edu
     * @param groupId The ID corresponding to the group the GroupMember entry is in.
     * @param userId The ID corresponding to the user linked to the GroupMember.
     * @param role The new GroupRole to be assigned in the GroupMember entry.
     * @return An updated data transfer object with Group details.
     */
    public GroupDTO changeUserPermission(Integer groupId, Integer userId, GroupRole role){
        User user = userRepository.findById(userId).orElseThrow();
        Group group = groupRepository.findById(userId).orElseThrow();
        GroupMember member = groupMemberRepository.findByUserAndGroup(user, group).orElseThrow();

        member.setRole(role);

        groupMemberRepository.save(member);

        return groupToGroupDTO(groupRepository.findById(groupId).get());
    }

    /**
     * Removes a Group entry from the DB.
     * @author Jonah Lorenzo jbl113@case.edu
     * @param groupId The ID of the group to be deleted.
     * @return A string that states that removal was succesful.
     */
    @Transactional
    public String deleteGroup(Integer groupId){
        groupRepository.deleteById(groupId);
        return "Group removed";
    }

    /**
     * Generated a data transfer object that contains the details of the provided group.
     * @author Jonah Lorenzo jbl113@case.edu
     * @param group The Group object to be transfered to a DTO.
     * @return A data transfer object containing details of the provided group: An Integer ID, String name, String creation timestamp, and two lists of GroupMemberDTO and GroupInviteDTO respectively.
     */
    private GroupDTO groupToGroupDTO(Group group){
        GroupDTO groupDTO = new GroupDTO(group.getName(),
                                         group.getId(),
                                         formatInstantToHTTP(group.getCreatedAt()),
                                         (List<GroupMemberDTO>) group.getMembers().stream()
                                            .map(member -> new GroupMemberDTO(
                                                member.getUser().getUsername(),
                                                member.getUser().getId(),
                                                member.getRole(),
                                                formatInstantToHTTP(member.getCreated_at())
                                            ))
                                            .collect(Collectors.toList()),
                                         (List<GroupInviteDTO1>) group.getInvites().stream()
                                            .map(invite -> new GroupInviteDTO1(
                                                invite.getUser().getUsername(),
                                                invite.getUser().getId(),
                                                formatInstantToHTTP(invite.getCreated_at())
                                            ))
                                            .collect(Collectors.toList()));

        return groupDTO;
    }

    /**
     * Converts a Java Instant object to a String formatted according to ISO 8601 standards
     * @author Jonah Lorenzo jbl113@case.edu
     * @param instant The Instant object to be converted
     * @return A String object with the information of instant in ISO 8601 format in GMT
     */
    private String formatInstantToHTTP(Instant instant) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
            "EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return dateFormat.format(Date.from(instant));
    }

}
