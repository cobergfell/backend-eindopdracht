package com.novi.fassignment.services;

import com.novi.fassignment.models.Question;
import com.novi.fassignment.repositories.QuestionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class QuestionServiceImplTest {

    // @InjectMocks vs @Mocks: see  https://howtodoinjava.com/mockito/mockito-mock-injectmocks/

//    @InjectMocks
//    private QuestionService questionService;

    //@InjectMocks QuestionService questionService = new QuestionService();
    @InjectMocks QuestionServiceImpl questionService;

    @Mock
    private QuestionRepository questionRepositoryMock;

    private Question question = new Question();

    @Test
    void questionServiceGetQuestionByIdTest() {
/*        Question question = new Question();
        question.setQuestionId(1L);*/
        Question expected = new Question();
        expected.setQuestionId(1L);
/*        Mockito.when(questionService.getQuestionById(expected.getQuestionId()))
                .thenReturn(expected);*/
/*        Mockito.when(questionService.getQuestionById(1L))
                .thenReturn(expected);*/
        Mockito.when(questionRepositoryMock.findById(1L))
                .thenReturn(Optional.of(expected));


        Question actual = questionService.getQuestionById(expected.getQuestionId());
        Assertions.assertSame(expected.getQuestionId(), actual.getQuestionId());
        //Assertions.assertSame(expected.getQuestionId(), question.getQuestionId());
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