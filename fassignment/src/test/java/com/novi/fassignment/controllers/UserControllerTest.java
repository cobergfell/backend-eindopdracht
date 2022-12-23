package com.novi.fassignment.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.novi.fassignment.controllers.dto.PaintingDto;
import com.novi.fassignment.controllers.dto.PaintingInputDto;
import com.novi.fassignment.controllers.dto.UserDto;
import com.novi.fassignment.models.Painting;
import com.novi.fassignment.models.Question;
import com.novi.fassignment.models.User;
import com.novi.fassignment.repositories.AuthorityRepository;
import com.novi.fassignment.repositories.PaintingRepository;
import com.novi.fassignment.repositories.QuestionRepository;
import com.novi.fassignment.repositories.UserRepository;
import com.novi.fassignment.services.*;
import com.novi.fassignment.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.security.config.http.MatcherType.mvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.mockito.Mockito.*;

import static org.assertj.core.api.Assertions.assertThat;


//@SpringBootTest(properties = {
//        "spring.jpa.hibernate.ddl-auto=create-drop",
//        "spring.liquibase.enabled=false",
//        "spring.flyway.enabled=false"
//})
@AutoConfigureMockMvc



@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = UserController.class)

public class UserControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    AuthorityRepository authorityRepository;

    @MockBean
    AuthorityService authorityService;

    @MockBean
    private QuestionRepository questionRepository;

    @MockBean
    private QuestionService questionService;

    @MockBean
    private QuestionServiceImpl questionServiceImpl;

    @MockBean
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private FileStorageInDataBaseServiceImpl storageService;

    @MockBean
    NoviMethod1FileUploadServiceImpl storageServiceOnDisc;

    @MockBean
    CustomUserDetailsService customUserDetailsService;

    @MockBean
    PaintingService paintingServiceMock;

    @MockBean
    PaintingServiceImpl paintingServiceImpl;

    @MockBean
    PaintingRepository paintingRepository;

    @MockBean
    JwtUtil jwtUtil;

    @MockBean
    Question question;

    @MockBean
    Painting painting;

    @MockBean
    PaintingDto paintingDto;

    @MockBean
    PaintingInputDto paintingInputDto;

    @MockBean
    User user;


    //@WithMockUser(username = "cobergfell")

//    @BeforeEach
//    void setup() {
//        User user = new User();
//        user.setUsername("cobergfell");
//        user.setPassword("mySecretPassword");
//        user.setEmail("fake@mail.com");
//        UserDto userDto=UserDto.fromUser(user);
//    }

    @Test
    void whenValidSignupInput_thenReturns201() throws Exception {
        User user = new User();
        user.setUsername("cobergfell");
        user.setPassword("mySecretPassword");
        user.setEmail("fake@mail.com");

        mockMvc.perform(post("/users/signup")
                .contentType(MediaType.valueOf("application/json"))
                //.contentType(MediaType.APPLICATION_JSON))
                .content(objectMapper.writeValueAsString(user)))
                //.andExpect(status().isOk());//would expect HTTP response code 200
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("cobergfell"))
                .andExpect(jsonPath("$.email").value("fake@mail.com"))
                .andDo(print());
    }



    @Test
    void whenValidInput_thenMapsToBusinessModel() throws Exception {
        User user = new User();
        user.setUsername("cobergfell");
        user.setPassword("mySecretPassword");
        user.setEmail("fake@mail.com");

        mockMvc.perform(post("/users/signup")
                .contentType(MediaType.valueOf("application/json"))
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andDo(print());

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userService, times(1)).createUser(userCaptor.capture());
        assertThat(userCaptor.getValue().getUsername()).isEqualTo("cobergfell");
        assertThat(userCaptor.getValue().getEmail()).isEqualTo("fake@mail.com");
    }



    @Test
    void whenValidInput_thenReturnsUserResource() throws Exception {
        User user = new User();
        user.setUsername("cobergfell");
        user.setPassword("mySecretPassword");

        MvcResult mvcResult = mockMvc.perform(post("/users/signup")
                .contentType(MediaType.valueOf("application/json"))
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn();

        User expectedResponseBody = user;
        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(
                objectMapper.writeValueAsString(expectedResponseBody));
    }




    @Test
    void whenValidInput_thenReturnsUserResource_withTypedAssertion() throws Exception {

        User user = new User();
        user.setUsername("cobergfell");
        user.setPassword("mySecretPassword");

        MvcResult mvcResult = mockMvc.perform(post("/users/signup")
                .contentType(MediaType.valueOf("application/json"))
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn();

        User expected = user;
        User actualResponseBody = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), User.class);
        assertThat(actualResponseBody.getUsername()).isEqualTo(expected.getUsername());
        assertThat(actualResponseBody.getEmail()).isEqualTo(expected.getEmail());

    }

    @Test
    void whenNullValue_thenReturns400() throws Exception {

        User user = new User();
        user.setPassword("mySecretPassword");
        user.setEmail("fake@mail.com");

        mockMvc.perform(post("/users/signup")
                .contentType(MediaType.valueOf("application/json"))
                .content(objectMapper.writeValueAsString(user)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

//     Test below not so interesting,
//    @Test
//    @WithMockUser(username = "cobergfell", password = "mySecretPassword")
//    public void whenValidSignInInput_thenReturns200() throws Exception {
//        mockMvc.perform(get("/users/signin"))
//                .andExpect(status().isOk())
//                //.andExpect(jsonPath("$.username").value("cobergfell"))
//                .andDo(print());
//    }


}

