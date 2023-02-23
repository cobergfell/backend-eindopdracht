package com.novi.fassignment.services;

import com.novi.fassignment.controllers.dto.QuestionDto;
import com.novi.fassignment.controllers.dto.UserDto;
import com.novi.fassignment.exceptions.RecordNotFoundException;
import com.novi.fassignment.models.Question;
import com.novi.fassignment.models.User;
import com.novi.fassignment.repositories.QuestionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class QuestionRepositoryTest {

    // @InjectMocks vs @Mocks: see  https://howtodoinjava.com/mockito/mockito-mock-injectmocks/


    @InjectMocks QuestionServiceImpl questionService;

    @Mock
    private QuestionRepository questionRepositoryMock;

    private Question question = new Question();

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
        Question newQuestion = new Question();
        newQuestion.setQuestionId(id);
        newQuestion.setTitle("myTestTitle");
        questionRepositoryMock.save(newQuestion);

        // when
        Optional<Question> questionOpt= questionRepositoryMock.findById(id);
        if (questionOpt.isPresent()) {
            Question question = questionOpt.get();

        // then
        String expected = "myTestTitle";
        String actual = question.getTitle();
        assertEquals(expected, actual);}

    }



}