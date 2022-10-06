package com.novi.fassignment.controllers.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.novi.fassignment.models.FileStoredInDataBase;
import com.novi.fassignment.models.MusicFileStoredInDataBase;
import com.novi.fassignment.models.Painting;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Lob;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

public class PaintingDto

{
    public Long paintingId;
    public String username;
    public String title;
    public String artist;
    //@Lob
    public String description;
    public byte[] image;


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

    public Set<FileStoredInDataBaseDto> attachedFiles = new HashSet<FileStoredInDataBaseDto>();
    public Set<MusicFileStoredInDataBaseDto> attachedMusicFiles = new HashSet<MusicFileStoredInDataBaseDto>();


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

    public  static PaintingDto fromPaintingToDto(Painting painting) {

        var dto = new PaintingDto();



/*        String s=String.valueOf(dto.id);
        String audioStorageUrl = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/files/")
                .path(s)
                .toUriString();*/

        dto.paintingId = painting.getPaintingId();
        dto.username = painting.getUser().getUsername();
        dto.title = painting.getTitle();
        dto.artist = painting.getArtist();
        dto.description = painting.getDescription();
        dto.image = painting.getImage();
        dto.dateTimePosted = painting.getDateTimePosted();
        dto.lastUpdate = painting.getLastUpdate();

        for (FileStoredInDataBase fileStoredInDataBase : painting.getFiles()) {
            FileStoredInDataBaseDto responseFileDto=FileStoredInDataBaseDto.fromFileStoredInDataBase(fileStoredInDataBase);
            dto.attachedFiles.add(responseFileDto);
        }


        for (MusicFileStoredInDataBase musicFileStoredInDataBase : painting.getMusicFiles()) {
            MusicFileStoredInDataBaseDto responseFileDto=MusicFileStoredInDataBaseDto.fromFileStoredInDataBase(musicFileStoredInDataBase);
            dto.attachedMusicFiles.add(responseFileDto);
//            String type=responseFileDto.getType();
//            boolean test=(type.equals("image/png"));
//            if (test){dto.image=responseFileDto.getData();}
        }

        return dto;



    }
}