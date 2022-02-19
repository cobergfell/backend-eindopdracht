package com.novi.fassignment.services;

import com.novi.fassignment.controllers.dto.QuestionInputDto;
import com.novi.fassignment.models.Question;

import java.util.List;

public interface QuestionService {
    List<Question> getAllQuestions();
    List<Question> getAllQuestionsByDescId();
    Question getQuestionById(Long id);
    Question createQuestionWithoutAttachment(Question question);
    void updateQuestion(QuestionInputDto dto, Question question);
    void createQuestion(QuestionInputDto dto);
    void deleteQuestionById(Long id);
    void deleteAllQuestions();
}