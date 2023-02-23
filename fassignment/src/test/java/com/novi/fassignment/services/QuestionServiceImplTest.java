package com.novi.fassignment.services;

import com.novi.fassignment.controllers.dto.QuestionDto;
import com.novi.fassignment.controllers.dto.QuestionDto;
import com.novi.fassignment.controllers.dto.UserDto;
import com.novi.fassignment.exceptions.RecordNotFoundException;
import com.novi.fassignment.models.Painting;
import com.novi.fassignment.models.Question;
import com.novi.fassignment.models.Question;
import com.novi.fassignment.models.User;
import com.novi.fassignment.repositories.QuestionRepository;
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
import static org.mockito.Mockito.when;

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

        when(questionRepositoryMock.findById(1L))
                .thenReturn(Optional.of(question));

        QuestionDto actual = questionService.getQuestionById(question.getQuestionId());
        Assertions.assertSame(question.getQuestionId(), actual.getQuestionId());
    }


    @Test
    void searchingUnknownIdShouldReturnErrorMessage() {
        long id = 1L;
        when(questionRepositoryMock.findById(id)).thenReturn(Optional.empty());
        //when
        final Executable executable = () -> questionService.getQuestionById(id);
        //assert
        assertThrows(RecordNotFoundException.class, executable);
    }



    @Test
    void whenSavedFromServiceThenFindsById() {
        long id = 1L;
        String myTitle = "myTitle";
        Question newQuestion = new Question();
        newQuestion.setQuestionId(id);
        newQuestion.setTitle(myTitle);


        //when
        questionService.createQuestionWithoutAttachment(newQuestion);

        //then
        String expected = myTitle;
        String actual = questionService.getQuestionById(id).getTitle();
        assertEquals(expected, actual);

    }

    @Test
    public void canCreateQuestion() {
        long id = 1L;
        Question testQuestion = new Question();
        when(questionRepositoryMock.save(testQuestion)).thenAnswer(invocation -> {
            Question question = invocation.getArgument(0);
            question.setQuestionId(id);
            return question;
        });
        Question created = questionService.createQuestionWithoutAttachment(testQuestion);
        assertEquals(id, created.getQuestionId());
    }


}