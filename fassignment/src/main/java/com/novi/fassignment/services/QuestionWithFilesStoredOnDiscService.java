package com.novi.fassignment.services;

import com.novi.fassignment.models.QuestionWithFilesStoredOnDisc;

import java.util.List;

public interface QuestionWithFilesStoredOnDiscService {
    List<QuestionWithFilesStoredOnDisc> getAllQuestionsWithFilesStoredOnDisc();
    QuestionWithFilesStoredOnDisc getQuestionWithFilesStoredOnDiscById(Long id);
    QuestionWithFilesStoredOnDisc createQuestionWithFilesStoredOnDisc(QuestionWithFilesStoredOnDisc question);
    void deleteQuestionWithFilesStoredOnDisc(Long id);

}