package org.swe.cart;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;
import org.swe.cart.embeddables.GroupMemberKey;
import org.swe.cart.entities.Group;
import org.swe.cart.entities.GroupMember;
import org.swe.cart.entities.GroupRole;
import org.swe.cart.entities.User;
import org.swe.cart.repositories.GroupMemberRepository;
import org.swe.cart.repositories.GroupRepository;
import org.swe.cart.repositories.UserRepository;
 
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class RepositoryTests {
 
    @Autowired
    private TestEntityManager entityManager;
     
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private GroupMemberRepository groupMemberRepository;
    
    @Test
    public void testCreateUser() {
        User user = new User();
        user.setEmail("ravikumar@gmail.com");
        user.setPassword("ravi2020");
        user.setUsername("Ravi");
     
        User savedUser = userRepository.save(user);
     
        User existUser = entityManager.find(User.class, savedUser.getId());
     
        assertThat(user.getEmail()).isEqualTo(existUser.getEmail());
    }

    @Test
    public void testCreateGroup(){
        Group group = new Group();
        group.setName("Test Group");
        User user = new User();
        user.setEmail("test@email.com");
        user.setPassword("password");
        user.setUsername("Test");

        userRepository.save(user);

        GroupMember member = new GroupMember();
        member.setUser(user);
        member.setGroup(group);
        member.setRole(GroupRole.ADMIN);

        GroupMemberKey key = new GroupMemberKey();
        key.setGroupid(group.getId());
        key.setUserid(user.getId());
        member.setId(key);

        groupMemberRepository.save(member);

        groupRepository.save(group);

        Group retrievedGroup = entityManager.find(Group.class, group.getId());

        assertThat(!retrievedGroup.getMembers().isEmpty());
    }
}
