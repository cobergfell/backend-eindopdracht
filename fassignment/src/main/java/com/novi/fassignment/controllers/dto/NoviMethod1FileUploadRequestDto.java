package com.novi.fassignment.controllers.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class NoviMethod1FileUploadRequestDto {
    private String title;
    private String description;
    private MultipartFile file;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
}

