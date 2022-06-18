package com.novi.fassignment.services;

import com.novi.fassignment.controllers.dto.AnswerInputDto;
import com.novi.fassignment.models.Answer;
import java.util.List;
import java.util.Set;

public interface AnswerService {
    List<Answer> getAllAnswers();
    List<Answer> getAllAnswersByDescId();
    Answer getAnswerById(Long id);
    Answer createAnswerWithoutAttachment(Answer answer);
    void updateAnswer(AnswerInputDto dto, Answer question);
    void createAnswer(AnswerInputDto dto);
    void deleteAnswerById(Long id);
    void deleteAllAnswers();
}