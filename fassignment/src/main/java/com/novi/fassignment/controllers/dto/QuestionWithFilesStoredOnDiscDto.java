package com.novi.fassignment.controllers.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.novi.fassignment.models.QuestionWithFilesStoredOnDisc;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


public class QuestionWithFilesStoredOnDiscDto {
    public Long questionId;
    public String title;
    public String content;
    public String tags;
    public String audioStoragePath;
    public String audioStorageUrl;
    public String audioStorageName;
    public String audioStorageType;
    public String audioStorageSize;
    public String username;

    @JsonSerialize
    UserDto userDto;

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
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

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getAudioStoragePath() {
        return audioStoragePath;
    }

    public void setAudioStoragePath(String audioStoragePath) {
        this.audioStoragePath = audioStoragePath;
    }

    public String getAudioStorageUrl() {
        return audioStorageUrl;
    }

    public void setAudioStorageUrl(String audioStorageUrl) {
        this.audioStorageUrl = audioStorageUrl;
    }

    public String getAudioStorageName() {
        return audioStorageName;
    }

    public void setAudioStorageName(String audioStorageName) {
        this.audioStorageName = audioStorageName;
    }

    public String getAudioStorageType() {
        return audioStorageType;
    }

    public void setAudioStorageType(String audioStorageType) {
        this.audioStorageType = audioStorageType;
    }

    public String getAudioStorageSize() {
        return audioStorageSize;
    }

    public void setAudioStorageSize(String audioStorageSize) {
        this.audioStorageSize = audioStorageSize;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UserDto getUserDto() {
        return userDto;
    }

    public void setUserDto(UserDto userDto) {
        this.userDto = userDto;
    }

    public static QuestionWithFilesStoredOnDiscDto fromQuestion(QuestionWithFilesStoredOnDisc question) {

        var dto = new QuestionWithFilesStoredOnDiscDto();

        String s=String.valueOf(dto.questionId);
        String audioStorageUrl = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/files/")
                .path(s)
                .toUriString();

        dto.questionId = question.getQuestionId();
        dto.title = question.getTitle();
        dto.content = question.getContent();
        dto.tags = question.getTags();
        dto.audioStoragePath = question.getAudioStoragePath();
        dto.audioStorageUrl = audioStorageUrl; //question.getAudioStorageUrl();
        dto.audioStorageName = question.getAudioStorageName();
        dto.audioStorageType = question.getAudioStorageType();
        dto.audioStorageSize = question.getAudioStorageSize();
        dto.username = question.getUser().getUsername();
        return dto;
    }
}