package com.novi.fassignment.services;

import com.novi.fassignment.controllers.dto.UserDto;
import com.novi.fassignment.models.Answer;
import com.novi.fassignment.models.User;
import com.novi.fassignment.repositories.AnswerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class AnswerRepositoryTest {

    // @InjectMocks vs @Mocks: see  https://howtodoinjava.com/mockito/mockito-mock-injectmocks/


    @InjectMocks AnswerServiceImpl answerService;

    @Mock
    private AnswerRepository answerRepositoryMock;

    private Answer answer = new Answer();

    @InjectMocks
    UserServiceImpl userService;

    private User user = new User();

    @BeforeEach
    void setup() {
        user.setUsername("cobergfell");
        user.setPassword("bla");
        user.setEmail("fake@mail.com");
        UserDto userDto=UserDto.fromUser(user);
    }



    @Test
    void whenSavedFromRepositoryThenFindsById() {
        long id = 1L;
        Answer newAnswer = new Answer();
        newAnswer.setAnswerId(id);
        newAnswer.setTitle("myTestTitle");
        answerRepositoryMock.save(newAnswer);

        // when
        Optional<Answer> answerOpt= answerRepositoryMock.findById(id);
        if (answerOpt.isPresent()) {
            Answer answer = answerOpt.get();

            // then
            String expected = "myTestTitle";
            String actual = answer.getTitle();
            assertEquals(expected, actual);}

    }



}