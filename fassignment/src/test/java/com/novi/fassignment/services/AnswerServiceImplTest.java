package com.novi.fassignment.services;

import com.novi.fassignment.controllers.dto.AnswerDto;
import com.novi.fassignment.controllers.dto.UserDto;
import com.novi.fassignment.models.Answer;
import com.novi.fassignment.models.User;
import com.novi.fassignment.repositories.AnswerRepository;
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
class AnswerServiceImplTest {

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
    void answerServiceGetAnswerByIdTest() {

        byte[] a =  {0xa, 0x2, (byte) 0xff};
        LocalDateTime dateTimePosted = LocalDateTime.now(ZoneId.of("GMT+00:01"));
        Answer answer = new Answer();
        answer.setAnswerId(1L);
        answer.setUser(user);
        answer.setTitle("myTitle");
        answer.setContent("");
        answer.setImage(a);
        answer.setDateTimePosted(dateTimePosted);
        answer.setLastUpdate(dateTimePosted);

        var dto = new AnswerDto();
        dto=AnswerDto.fromAnswerToDto(answer);

        Mockito.when(answerRepositoryMock.findById(1L))
                .thenReturn(Optional.of(answer));

        AnswerDto actual = answerService.getAnswerById(answer.getAnswerId());
        Assertions.assertSame(answer.getAnswerId(), actual.getAnswerId());
    }


    @Test
    void searchingUnknownIdShouldReturnErrorMessage() {
        long id = 1L;
        Mockito.when(answerRepositoryMock.findById(id)).thenReturn(Optional.empty());
        try {
            answerService.getAnswerById(id);
        } catch (Exception e) {
            Assertions.assertEquals("id does not exist", e.getMessage());
        }
    }


    @Test
    void whenSavedFromRepositoryThenFindsById() {
        Answer newAnswer = new Answer();
        newAnswer.setAnswerId(1L);
        answerRepositoryMock.save(newAnswer);
        Assertions.assertNotNull(answerRepositoryMock.findById(newAnswer.getAnswerId()));
    }


    @Test
    void whenSavedFromServiceThenFindsById() {
        Answer newAnswer = new Answer();
        newAnswer.setAnswerId(1L);
        answerService.createAnswerWithoutAttachment(newAnswer);
        Assertions.assertNotNull(answerRepositoryMock.findById(newAnswer.getAnswerId()));
    }


}