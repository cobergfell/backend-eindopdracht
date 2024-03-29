package com.novi.fassignment.controllers.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.novi.fassignment.models.Painting;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

public class QuestionInputDto {

        public Long questionId;//questionId will only be specified when updating
        public String username;
        public String title;
        public String content;
        public Long idRelatedItem;
        public Painting painting;
        public byte[] image;
        public MultipartFile[] musicFiles;
        public MultipartFile[] files;


//        @CreationTimestamp
//        @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+00:01")
//        public ZonedDateTime dateTimePosted;
//        @UpdateTimestamp
//        @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+00:01")
//        public ZonedDateTime lastUpdate;

        @CreationTimestamp
        @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
        public LocalDateTime dateTimePosted;
        @UpdateTimestamp
        @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
        public LocalDateTime lastUpdate;

        public Long getQuestionId() {
                return questionId;
        }

        public void setQuestionId(Long questionId) {
                this.questionId = questionId;
        }

        public String getUsername() {
                return username;
        }

        public void setUsername(String username) {
                this.username = username;
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

        public Long getIdRelatedItem() {
                return idRelatedItem;
        }

        public void setIdRelatedItem(Long idRelatedItem) {
                this.idRelatedItem = idRelatedItem;
        }

        public Painting getPainting() {
                return painting;
        }

        public void setPainting(Painting painting) {
                this.painting = painting;
        }

        public byte[] getImage() {
                return image;
        }

        public void setImage(byte[] image) {
                this.image = image;
        }

        public MultipartFile[] getMusicFiles() {
                return musicFiles;
        }

        public void setMusicFiles(MultipartFile[] musicFiles) {
                this.musicFiles = musicFiles;
        }

        public MultipartFile[] getFiles() {
                return files;
        }

        public void setFiles(MultipartFile[] files) {
                this.files = files;
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
}




