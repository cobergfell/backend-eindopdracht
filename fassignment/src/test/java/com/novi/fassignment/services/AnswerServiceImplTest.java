package com.novi.fassignment.services;

import com.novi.fassignment.controllers.dto.AnswerDto;
import com.novi.fassignment.controllers.dto.UserDto;
import com.novi.fassignment.exceptions.RecordNotFoundException;
import com.novi.fassignment.models.Answer;
import com.novi.fassignment.models.Question;
import com.novi.fassignment.models.User;
import com.novi.fassignment.repositories.AnswerRepository;
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
        //when
        final Executable executable = () -> answerService.getAnswerById(id);
        //assert
        assertThrows(RecordNotFoundException.class, executable);
    }



    @Test
    public void canCreateAnswer() {
        long id = 1L;
        Answer testAnswer = new Answer();
        when(answerRepositoryMock.save(testAnswer)).thenAnswer(invocation -> {
            Answer answer = invocation.getArgument(0);
            answer.setAnswerId(id);
            return answer;
        });
        Answer created = answerService.createAnswerWithoutAttachment(testAnswer);
        assertEquals(id, created.getAnswerId());
    }



}