package com.novi.fassignment.controllers.dto;

import com.novi.fassignment.models.Answer;
import com.novi.fassignment.models.Painting;
import com.novi.fassignment.models.Question;
import org.springframework.web.multipart.MultipartFile;


public class MusicFileStoredInDataBaseInputDto {

    //public MultipartFile[] file;
    private Long id;//only for update purpose to check if a file is already existing
    public MultipartFile file;
    public Question question;
    public Answer answer;
    public Painting painting;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public Answer getAnswer() {
        return answer;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
    }

    public Painting getPainting() {
        return painting;
    }

    public void setPainting(Painting painting) {
        this.painting = painting;
    }

}