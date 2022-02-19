package com.novi.fassignment.controllers.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.novi.fassignment.models.MusicPiece;
import com.novi.fassignment.models.Painting;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Date;

public class QuestionInputDto {

        public Long questionId;//questionId will only be specified when updating
        public String username;
        public String title;
        public String content;
        public String tags;
        public String questionRelatedTo;
        public Long idRelatedItem;
        public Painting painting;
        public MusicPiece musicPiece;
        public MultipartFile[] files;
        @CreationTimestamp
        @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+00:00")
        public ZonedDateTime dateTimePosted;
        @UpdateTimestamp
        @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+00:00")
        public ZonedDateTime lastUpdate;

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

        public String getTags() {
                return tags;
        }

        public void setTags(String tags) {
                this.tags = tags;
        }

        public String getQuestionRelatedTo() {
                return questionRelatedTo;
        }

        public void setQuestionRelatedTo(String questionRelatedTo) {
                this.questionRelatedTo = questionRelatedTo;
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

        public MusicPiece getMusicPiece() {
                return musicPiece;
        }

        public void setMusicPiece(MusicPiece musicPiece) {
                this.musicPiece = musicPiece;
        }

        public MultipartFile[] getFiles() {
                return files;
        }

        public void setFiles(MultipartFile[] files) {
                this.files = files;
        }

        public ZonedDateTime getDateTimePosted() {
                return dateTimePosted;
        }

        public void setDateTimePosted(ZonedDateTime dateTimePosted) {
                this.dateTimePosted = dateTimePosted;
        }

        public ZonedDateTime getLastUpdate() {
                return lastUpdate;
        }

        public void setLastUpdate(ZonedDateTime lastUpdate) {
                this.lastUpdate = lastUpdate;
        }
}




