package com.novi.fassignment.controllers;


import com.novi.fassignment.models.User;
import com.novi.fassignment.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.assertj.core.api.AbstractAssert;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


//adapted from https://github.com/thombergs/code-examples/blob/master/spring-boot/spring-boot-testing/src/test/java/io/reflectoring/testing/RegisterUseCaseIntegrationTest.java

// uncomment below lines to use an H2 embedded database instead of my postgres production database (stil need to be fixed)
//@TestPropertySource(
//        locations = "classpath:application-integrationtest.properties")

//@SpringBootTest(properties = {
//        "spring.jpa.hibernate.ddl-auto=create-drop",
//        "spring.liquibase.enabled=false",
//        "spring.flyway.enabled=false"
//})


@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void registrationWorksThroughAllLayers() throws Exception {
        User user = new User();
        user.setUsername("cobergfellTest");
        user.setPassword("mySecretPassword");
        user.setEmail("fake@mail.com");

        mockMvc.perform(post("/users/signup")
                .contentType(MediaType.valueOf("application/json"))
                //.contentType(MediaType.APPLICATION_JSON))
                .content(objectMapper.writeValueAsString(user)))
                //.andExpect(status().isOk());//would expect HTTP response code 200
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("cobergfellTest"))
                .andExpect(jsonPath("$.email").value("fake@mail.com"))
                .andDo(print());

        Optional<User> userEntityOpt = userRepository.findById("cobergfellTest");
        assertThat(userEntityOpt.get().getEmail()).isEqualTo("fake@mail.com");
    }

}