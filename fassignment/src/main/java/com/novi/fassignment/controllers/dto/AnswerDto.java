package com.novi.fassignment.controllers.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.novi.fassignment.models.FileStoredInDataBase;
import com.novi.fassignment.models.Answer;
import com.novi.fassignment.models.MusicFileStoredInDataBase;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

public class AnswerDto

{
    public Long answerId;
    public String title;
    public String content;
    public String username;
    public byte[] image;
    public Long questionId;

    @CreationTimestamp
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    public LocalDateTime dateTimePosted;
    @UpdateTimestamp
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    public LocalDateTime lastUpdate;


    public Set<FileStoredInDataBaseDto> attachedFiles = new HashSet<FileStoredInDataBaseDto>();
    public Set<MusicFileStoredInDataBaseDto> attachedMusicFiles = new HashSet<>();


    public Long getAnswerId() {
        return answerId;
    }

    public void setAnswerId(Long answerId) {
        this.answerId = answerId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }


    public LocalDateTime getDateTimePosted() {
        return dateTimePosted;
    }

    public void setDateTimePosted(LocalDateTime dateTimePosted) {
        this.dateTimePosted = dateTimePosted;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Set<FileStoredInDataBaseDto> getAttachedFiles() {
        return attachedFiles;
    }

    public void setAttachedFiles(Set<FileStoredInDataBaseDto> attachedFiles) {
        this.attachedFiles = attachedFiles;
    }

    public Set<MusicFileStoredInDataBaseDto> getAttachedMusicFiles() {
        return attachedMusicFiles;
    }

    public void setAttachedMusicFiles(Set<MusicFileStoredInDataBaseDto> attachedMusicFiles) {
        this.attachedMusicFiles = attachedMusicFiles;
    }

    public  static AnswerDto fromAnswerToDto(Answer answer) {
        var dto = new AnswerDto();

        dto.answerId = answer.getAnswerId();
        dto.title = answer.getTitle();
        dto.content = answer.getContent();
        //dto.tags = answer.getTags();
        dto.dateTimePosted = answer.getDateTimePosted();
        dto.lastUpdate = answer.getLastUpdate();
        dto.username = answer.getUser().getUsername();
        try{dto.questionId = answer.getQuestion().getQuestionId();}
        catch (Exception exception) {dto.questionId =null;}


        for (FileStoredInDataBase fileStoredInDataBase : answer.getFiles()) {
            FileStoredInDataBaseDto responseFileDto=FileStoredInDataBaseDto.fromFileStoredInDataBase(fileStoredInDataBase);
            dto.attachedFiles.add(responseFileDto);
        }


        for (MusicFileStoredInDataBase musicFileStoredInDataBase : answer.getMusicFiles()) {
            MusicFileStoredInDataBaseDto responseFileDto=MusicFileStoredInDataBaseDto.fromFileStoredInDataBase(musicFileStoredInDataBase);
            dto.attachedMusicFiles.add(responseFileDto);
        }

        return dto;



    }
}