package com.novi.fassignment.services;

import com.novi.fassignment.controllers.dto.AnswerDto;
import com.novi.fassignment.controllers.dto.AnswerInputDto;
import com.novi.fassignment.models.Answer;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public interface AnswerService {
    List<AnswerDto> getAllAnswers();
    AnswerDto getAnswerById(Long id);
    List<AnswerDto> getAnswersByQuestionId(Long questionId);
    Answer createAnswerWithoutAttachment(Answer answer);
    void updateAnswer(AnswerInputDto dto);
    void createAnswer(AnswerInputDto dto);
    void deleteAnswerById(Long id);
    void deleteAllAnswers();
}