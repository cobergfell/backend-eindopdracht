package com.novi.fassignment.services;

import com.novi.fassignment.exceptions.RecordNotFoundException;
import com.novi.fassignment.models.QuestionWithFilesStoredOnDisc;
import com.novi.fassignment.repositories.QuestionWithFilesStoredOnDiscRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionWithFilesStoredOnDiscServiceImpl implements QuestionWithFilesStoredOnDiscService {
    private QuestionWithFilesStoredOnDiscRepository questionRepository;
    private List<QuestionWithFilesStoredOnDisc> Questions = new ArrayList<>();

    @Autowired
    public QuestionWithFilesStoredOnDiscServiceImpl(QuestionWithFilesStoredOnDiscRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public List<QuestionWithFilesStoredOnDisc> getAllQuestionsWithFilesStoredOnDisc() {
        return questionRepository.findAll();
    }

    @Override
    public QuestionWithFilesStoredOnDisc createQuestionWithFilesStoredOnDisc(QuestionWithFilesStoredOnDisc question) {
        return questionRepository.save(question);
    }

    @Override
    public void deleteQuestionWithFilesStoredOnDisc(Long id) {
        questionRepository.deleteById(id);
    }


    @Override
    public QuestionWithFilesStoredOnDisc getQuestionWithFilesStoredOnDiscById(Long id) {
        var optionalQuestion = questionRepository.findById(id);
        if (optionalQuestion.isPresent()) {
            return optionalQuestion.get();
        } else {
            throw new RecordNotFoundException("id does not exist");
        }
    }
    //start modification
    //private List<Question> Questions = new ArrayList<>();

/*    @Override
    public Question getQuestionById(long id) {
        Question question = null;
        for (int i=0; i<questions.size(); i++) {
            if (id == questions.get(i).getId()) {
                question = questions.get(i);
            }
        }
        if (question == null) {
            throw new RecordNotFoundException("id does not exist");
        }
        return Question;
    }*/

}

