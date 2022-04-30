package com.novi.fassignment.controllers.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.novi.fassignment.models.FileStoredInDataBase;
import com.novi.fassignment.models.Answer;
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
    public String tags;
    public String username;
    public Long questionId;
    public Long paintingId;
    public Long musicPieceId;

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

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
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

    public Long getPaintingId() {
        return paintingId;
    }

    public void setPaintingId(Long paintingId) {
        this.paintingId = paintingId;
    }

    public Long getMusicPieceId() {
        return musicPieceId;
    }

    public void setMusicPieceId(Long musicPieceId) {
        this.musicPieceId = musicPieceId;
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

    public  static AnswerDto fromAnswerToDto(Answer answer) {
        var dto = new AnswerDto();

/*        String s=String.valueOf(dto.id);
        String audioStorageUrl = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/files/")
                .path(s)
                .toUriString();*/

        dto.answerId = answer.getAnswerId();
        dto.title = answer.getTitle();
        dto.content = answer.getContent();
        dto.tags = answer.getTags();
        dto.dateTimePosted = answer.getDateTimePosted();
        dto.lastUpdate = answer.getLastUpdate();
        dto.username = answer.getUser().getUsername();
        try{dto.questionId = answer.getQuestion().getQuestionId();}
        catch (Exception exception) {dto.questionId =null;}
        try{dto.paintingId= answer.getPainting().getPaintingId();}
        catch (Exception exception) {dto.paintingId =null;}

        //How to filter a map, example 1 by converting a list into stream and apply the stream filter method
        //see https://mkyong.com/java8/java-8-streams-filter-examples/
        //List<FileStoredInDataBase> filteredFiles_example1=filesStoredInDataBase.stream()
        //.filter(dbFile -> dbFile.getQuestion().getQuestionId().equals(question.getQuestionId()))
        //.collect(Collectors.toList());

        //How to filter a map, example 2, older method before java 8,  using the list only
        //see https://mkyong.com/java8/java-8-streams-filter-examples/
        //List<FileStoredInDataBase> filteredFiles_example2 = new ArrayList<>();
        //for (FileStoredInDataBase fileStoredInDataBase : filteredFiles_example2) {
        //if (fileStoredInDataBase.getQuestion().getQuestionId().equals(question.getQuestionId())) {
        //filteredFiles_example2.add(fileStoredInDataBase);
        //}
        //}

        //Finally, we do it this way (here, we also directly map the filtered files into the files Dto's
        //List<ResponseFileDto> filteredFiles = filesStoredInDataBase.stream()
        //.filter(dbFile -> dbFile.getQuestion().getQuestionId().equals(question.getQuestionId()))
        //.map(dbFile -> ResponseFileDto.fromFileStoredInDataBase(dbFile))
        //.collect(Collectors.toList());
/*        for (FileStoredInDataBase fileStoredInDataBase : question.getFiles()) {
            dto.responseFileDtos.add(ResponseFileDto.fromFileStoredInDataBase(fileStoredInDataBase));
        }*/

        for (FileStoredInDataBase fileStoredInDataBase : answer.getFiles()) {
            FileStoredInDataBaseDto responseFileDto=FileStoredInDataBaseDto.fromFileStoredInDataBase(fileStoredInDataBase);
            dto.attachedFiles.add(responseFileDto);
        }

        return dto;



    }
}