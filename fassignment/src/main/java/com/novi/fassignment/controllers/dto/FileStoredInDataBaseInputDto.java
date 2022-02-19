package com.novi.fassignment.controllers.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.novi.fassignment.models.Answer;
import com.novi.fassignment.models.MusicPiece;
import com.novi.fassignment.models.Painting;
import com.novi.fassignment.models.Question;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Date;
import java.time.ZonedDateTime;



public class FileStoredInDataBaseInputDto {

    //public MultipartFile[] file;
    public MultipartFile file;
    public Question question;
    public Answer answer;
    public Painting painting;
    public MusicPiece musicPiece;

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

    public MusicPiece getMusicPiece() {
        return musicPiece;
    }

    public void setMusicPiece(MusicPiece musicPiece) {
        this.musicPiece = musicPiece;
    }
}