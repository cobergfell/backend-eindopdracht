package com.novi.fassignment.services;

import com.novi.fassignment.controllers.dto.QuestionDto;
import com.novi.fassignment.controllers.dto.QuestionDto;
import com.novi.fassignment.controllers.dto.UserDto;
import com.novi.fassignment.models.Question;
import com.novi.fassignment.models.Question;
import com.novi.fassignment.models.User;
import com.novi.fassignment.repositories.QuestionRepository;
import com.novi.fassignment.repositories.QuestionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class QuestionServiceImplTest {

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
    void questionServiceGetQuestionByIdTest() {

        byte[] a =  {0xa, 0x2, (byte) 0xff};
        LocalDateTime dateTimePosted = LocalDateTime.now(ZoneId.of("GMT+00:01"));
        Question question = new Question();
        question.setQuestionId(1L);
        question.setUser(user);
        question.setTitle("myTitle");
        question.setContent("");
        question.setImage(a);
        question.setDateTimePosted(dateTimePosted);
        question.setLastUpdate(dateTimePosted);

        var dto = new QuestionDto();
        dto=QuestionDto.fromQuestionToDto(question);

        Mockito.when(questionRepositoryMock.findById(1L))
                .thenReturn(Optional.of(question));

        QuestionDto actual = questionService.getQuestionById(question.getQuestionId());
        Assertions.assertSame(question.getQuestionId(), actual.getQuestionId());
    }


    @Test
    void searchingUnknownIdShouldReturnErrorMessage() {
        long id = 1L;
        Mockito.when(questionRepositoryMock.findById(id)).thenReturn(Optional.empty());
        try {
            questionService.getQuestionById(id);
        } catch (Exception e) {
            Assertions.assertEquals("id does not exist", e.getMessage());
        }
    }


    @Test
    void whenSavedFromRepositoryThenFindsById() {
        Question newQuestion = new Question();
        newQuestion.setQuestionId(1L);
        questionRepositoryMock.save(newQuestion);
        Assertions.assertNotNull(questionRepositoryMock.findById(newQuestion.getQuestionId()));
    }


    @Test
    void whenSavedFromServiceThenFindsById() {
        Question newQuestion = new Question();
        newQuestion.setQuestionId(1L);
        questionService.createQuestionWithoutAttachment(newQuestion);
        Assertions.assertNotNull(questionRepositoryMock.findById(newQuestion.getQuestionId()));
    }


}