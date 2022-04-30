package com.novi.fassignment.controllers.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Date;

public class PaintingInputDto {

    public Long paintingId;//paintingId will only be specified when updating
    public String username;
    public String title;
    public String artist;
    public String description;
    public byte[] image;
    public MultipartFile[] musicFiles;
    public MultipartFile[] files;

//    @CreationTimestamp
//    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+00:01")
//    public ZonedDateTime dateTimePosted;
//    @UpdateTimestamp
//    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+00:01")
//    public ZonedDateTime lastUpdate;

    @CreationTimestamp
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    public LocalDateTime dateTimePosted;
    @UpdateTimestamp
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    public LocalDateTime lastUpdate;

    public Long getPaintingId() {
        return paintingId;
    }

    public void setPaintingId(Long paintingId) {
        this.paintingId = paintingId;
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

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
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

    public MultipartFile[] getMusicFiles() {
        return musicFiles;
    }

    public void setMusicFiles(MultipartFile[] musicFiles) {
        this.musicFiles = musicFiles;
    }
}





