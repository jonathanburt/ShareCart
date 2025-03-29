package org.swe.cart;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.swe.cart.security.JwtUtil;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;

@AutoConfigureJsonTesters
@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class UserControllerMockMVCwithContextTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Test
    public void canCreateUserWhenNameAndEmailDontExist() throws Exception{
        String jsonBody = "{\"username\" : \"Jonah\", \"email\" : \" jbl113@case.edu\", \"password\" : \"password\"}";
        

        MockHttpServletResponse response = mvc.perform(MockMvcRequestBuilders.post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody))
            .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void canNotCreateUserWhenNameExistsAndEmailUnique() throws Exception{
        String jsonBody1 = "{\"username\" : \"Jonah\", \"email\" : \" jbl113@case.edu\", \"password\" : \"password\"}";
        String jsonBody2 = "{\"username\" : \"Jonah\", \"email\" : \" jbl114@case.edu\", \"password\" : \"password\"}";

        MockHttpServletResponse response = mvc.perform(MockMvcRequestBuilders.post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody1))
            .andReturn().getResponse();

        MockHttpServletResponse response2 = mvc.perform(MockMvcRequestBuilders.post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody2))
            .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response2.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void canNotCreateUserWhenNameuniqueAndEmailExists() throws Exception{
        String jsonBody1 = "{\"username\" : \"Jonah\", \"email\" : \" jbl113@case.edu\", \"password\" : \"password\"}";
        String jsonBody2 = "{\"username\" : \"Jonah2\", \"email\" : \" jbl113@case.edu\", \"password\" : \"password\"}";

        MockHttpServletResponse response = mvc.perform(MockMvcRequestBuilders.post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody1))
            .andReturn().getResponse();

        MockHttpServletResponse response2 = mvc.perform(MockMvcRequestBuilders.post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody2))
            .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response2.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void canLogInWithValidCredentials() throws Exception{
        String jsonBody1 = "{\"username\" : \"Jonah\", \"email\" : \" jbl113@case.edu\", \"password\" : \"password\"}";

        MockHttpServletResponse response = mvc.perform(MockMvcRequestBuilders.post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody1))
            .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        String jsonBody2 = "{\"username\" : \"Jonah\", \"password\" : \"password\"}";

        MockHttpServletResponse response2 = mvc.perform(MockMvcRequestBuilders.get("/api/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody2))
            .andReturn().getResponse();

        assertThat(response2.getStatus()).isEqualTo(HttpStatus.OK.value());

        String responseString = response2.getContentAsString();
        JsonNode jsonNode = objectMapper.readTree(responseString);
        String token = jsonNode.get("token").asText();

        assertThat(jwtUtil.validateTokenAndRetrieveUsername(token)).isEqualTo("Jonah");
    }
}
