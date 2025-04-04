package org.swe.cart;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.swe.cart.entities.Group;
import org.swe.cart.repositories.GroupRepository;
import org.swe.cart.repositories.ItemRepository;
import org.swe.cart.security.JwtUtil;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(MockitoExtension.class)
@Transactional
public class ItemControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Test
    public void cannotCreateItemWhenGroupIdIsNull() throws Exception{
        String createGroupJson = "{\"name\":\"test100\"}";
        String jwt = loginResponse.get("token").asText();
        ResponseEntity<String> returnedGroupResponse = restTemplate.postForEntity("/api/group/create", new HttpEntity<>(createGroupJson, getJsonHeaders(jwt)), String.class);

        assertThat(returnedGroupResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        Integer returnedGroupId = objectMapper.readTree(returnedGroupResponse.getBody()).get("id").asInt();

        Optional<Group> returnedGroupOptional = groupRepository.findById(returnedGroupId);

        assertThat(returnedGroupOptional.isPresent());

        String jsonBody1 = "{\"name\": \"Jeremy\", \"description\": \"description\", \"price\": 10, \"group_id\": null}";
        

    }
    
    
}
