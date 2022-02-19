package com.novi.fassignment.models;

import javax.persistence.*;

@Entity
@Table(name = "questions_with_files_stored_on_disc")
public class QuestionWithFilesStoredOnDisc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long questionId;
    //private String datePosted;
    private String title;
    private String content;
    private String tags;
    private String audioStoragePath;
    private String audioStorageUrl;
    private String audioStorageName;
    private String audioStorageType;
    private String audioStorageSize;

    @Lob
    byte[] audioStorage;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "username")
    private User user;


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

    public byte[] getAudioStorage() {
        return audioStorage;
    }

    public void setAudioStorage(byte[] audioStorage) {
        this.audioStorage = audioStorage;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

