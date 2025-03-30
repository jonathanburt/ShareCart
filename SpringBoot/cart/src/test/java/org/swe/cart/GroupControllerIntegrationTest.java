package org.swe.cart;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.swe.cart.entities.Group;
import org.swe.cart.entities.GroupMember;
import org.swe.cart.entities.Role;
import org.swe.cart.repositories.GroupRepository;
import org.swe.cart.repositories.UserRepository;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ExtendWith(MockitoExtension.class)
@Transactional
public class GroupControllerIntegrationTest {
    
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void canCreateGroupWhenLoggedIn() throws Exception{
        createAccount("Jonah", "jbl113@case.edu", "password");
        JsonNode loginResponse = login("Jonah", "password");

        String jwt = loginResponse.get("token").asText();
        Integer userId = loginResponse.get("userId").asInt();

        String createGroupJson = "{\"name\":\"test100\"}";

        ResponseEntity<String> returnedGroupResponse = restTemplate.postForEntity("/api/group/create", new HttpEntity<>(createGroupJson, getJsonHeaders(jwt)), String.class);

        assertThat(returnedGroupResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        Integer returnedGroupId = objectMapper.readTree(returnedGroupResponse.getBody()).get("id").asInt();

        Optional<Group> returnedGroupOptional = groupRepository.findById(returnedGroupId);

        assertThat(returnedGroupOptional.isPresent()); //Assert that a group was created and the correct Id returned

        Group returnedGroup = returnedGroupOptional.get();

        assertThat(returnedGroup.getMembers().size()).isEqualTo(1); //Esnure that there is only one member in the group

        GroupMember creator = returnedGroup.getMembers().iterator().next(); //Get the first item from the set
        
        assertThat(creator.getRole()).isEqualTo(Role.ADMIN); //Assert that the group creator is an admin

        assertThat(creator.getUser().getId()).isEqualTo(userId); //Assert that the group creator is the the user who is logged in
    }

    private void createAccount(String username, String email, String password){
        String jsonBody1 = String.format("{\"username\": \"%s\", \"email\": \"%s\", \"password\":\"%s\"}", username, email, password);

        restTemplate.postForEntity("/api/auth/signup", new HttpEntity<>(jsonBody1, getJsonHeaders(null)), String.class);
    }

    private JsonNode login(String username, String password) throws Exception{
        String jsonBody2 = String.format("{\"username\": \"%s\", \"password\": \"%s\"}", username, password);

        ResponseEntity<String> response2 = restTemplate.exchange("/api/auth/signin",
                HttpMethod.GET,
                new HttpEntity<>(jsonBody2, getJsonHeaders(null)),
                String.class);


        String responseString = response2.getBody();
        System.out.println(responseString);
        JsonNode jsonNode = objectMapper.readTree(responseString);
        return jsonNode;
    }

    private HttpHeaders getJsonHeaders(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if(token != null){
            headers.setBearerAuth(token);
        }
        return headers;
    }

}
