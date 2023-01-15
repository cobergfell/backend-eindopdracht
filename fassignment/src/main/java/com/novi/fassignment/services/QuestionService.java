package com.novi.fassignment.services;

import com.novi.fassignment.controllers.dto.QuestionDto;
import com.novi.fassignment.controllers.dto.QuestionInputDto;
import com.novi.fassignment.models.Question;

import java.util.List;

public interface QuestionService {
    List<QuestionDto> getAllQuestions();
    QuestionDto getQuestionById(Long id);
    List<QuestionDto> getQuestionsByPaintingId(Long paintingId);
    Question createQuestionWithoutAttachment(Question question);
    void updateQuestion(QuestionInputDto dto, Question question);
    void createQuestion(QuestionInputDto dto);
    void deleteQuestionById(Long id);
    void deleteAllQuestions();
}