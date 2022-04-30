package com.novi.fassignment.controllers.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.novi.fassignment.models.FileStoredInDataBase;
import com.novi.fassignment.models.Question;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

public class QuestionDto

{
    public Long questionId;
    public String title;
    public String content;
    public String tags;
    public String username;

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public  static QuestionDto fromQuestionToDto(Question question) {

        var dto = new QuestionDto();

/*        String s=String.valueOf(dto.id);
        String audioStorageUrl = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/files/")
                .path(s)
                .toUriString();*/

        dto.questionId = question.getQuestionId();
        dto.title = question.getTitle();
        dto.content = question.getContent();
        dto.tags = question.getTags();
        dto.dateTimePosted = question.getDateTimePosted();
        dto.lastUpdate = question.getLastUpdate();
        dto.username = question.getUser().getUsername();

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

        for (FileStoredInDataBase fileStoredInDataBase : question.getFiles()) {
            FileStoredInDataBaseDto responseFileDto=FileStoredInDataBaseDto.fromFileStoredInDataBase(fileStoredInDataBase);
            dto.attachedFiles.add(responseFileDto);
        }

        return dto;



        }
}