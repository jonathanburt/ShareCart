package org.swe.cart;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.annotation.Rollback;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.swe.cart.entities.User;
import org.swe.cart.repositories.UserRepository;
import org.swe.cart.security.JwtUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // Starts full Spring Boot server
@ExtendWith(MockitoExtension.class)
@Transactional
@Rollback(true)
public class UserControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;
    //Test names are UCT-i where i is the number the of the test in the file

    @Test
    public void canCreateUserWhenNameAndEmailDontExist() {
        String jsonBody = "{\"username\": \"Jonah\", \"email\": \"jbl113@case.edu\", \"password\": \"password\"}";

        ResponseEntity<String> response = restTemplate.postForEntity("/api/auth/signup",
                new HttpEntity<>(jsonBody, getJsonHeaders()), String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void canNotCreateUserWhenNameExistsAndEmailUnique() {
        String jsonBody1 = "{\"username\": \"Jonah\", \"email\": \"jbl113@case.edu\", \"password\": \"password\"}";
        String jsonBody2 = "{\"username\": \"Jonah\", \"email\": \"jbl114@case.edu\", \"password\": \"password\"}";

        restTemplate.postForEntity("/api/auth/signup", new HttpEntity<>(jsonBody1, getJsonHeaders()), String.class);
        ResponseEntity<String> response2 = restTemplate.postForEntity("/api/auth/signup",
                new HttpEntity<>(jsonBody2, getJsonHeaders()), String.class);

        assertThat(response2.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void canNotCreateUserWhenNameUniqueAndEmailExists() {
        String jsonBody1 = "{\"username\": \"Jonah\", \"email\": \"jbl113@case.edu\", \"password\": \"password\"}";
        String jsonBody2 = "{\"username\": \"Jonah2\", \"email\": \"jbl113@case.edu\", \"password\": \"password\"}";

        restTemplate.postForEntity("/api/auth/signup", new HttpEntity<>(jsonBody1, getJsonHeaders()), String.class);
        ResponseEntity<String> response2 = restTemplate.postForEntity("/api/auth/signup",
                new HttpEntity<>(jsonBody2, getJsonHeaders()), String.class);

        assertThat(response2.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void canLogInWithValidCredentials() throws Exception {
        String username = "Jonah";
        String jsonBody1 = String.format("{\"username\": \"%s\", \"email\": \"jbl113@case.edu\", \"password\": \"password\"}", username);

        restTemplate.postForEntity("/api/auth/signup", new HttpEntity<>(jsonBody1, getJsonHeaders()), String.class);

        String jsonBody2 = String.format("{\"username\": \"%s\", \"password\": \"password\"}", username);

        ResponseEntity<String> response2 = restTemplate.exchange("/api/auth/signin",
                HttpMethod.POST,
                new HttpEntity<>(jsonBody2, getJsonHeaders()),
                String.class);

        assertThat(response2.getStatusCode()).isEqualTo(HttpStatus.OK);

        String responseString = response2.getBody();
        System.out.println(responseString);
        JsonNode jsonNode = objectMapper.readTree(responseString);
        String token = jsonNode.get("token").asText();
        Integer userId = jsonNode.get("userId").asInt();

        User user = userRepository.findByUsername(username).orElseThrow();

        assertThat(jwtUtil.validateTokenAndRetrieveUsername(token)).isEqualTo(username);
        assertThat(userId.equals(user.getId()));
    }

    private HttpHeaders getJsonHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}
