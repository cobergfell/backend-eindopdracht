package com.novi.fassignment.controllers.dto;

import com.novi.fassignment.models.FileStoredInDataBase;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.persistence.Lob;


public class FileStoredInDataBaseDto {
    private Long id;
    private String name;
    private String type;
    private String url;
    private int size;

    @Lob
    private byte[] data;


    //do not forget the getters and setters with Dto's


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public static FileStoredInDataBaseDto fromFileStoredInDataBase(FileStoredInDataBase fileStoredInDataBase) {

        var dto = new FileStoredInDataBaseDto();
        String fileDownloadUri = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/files_database/")
                .path(String.valueOf(fileStoredInDataBase.getFileId()))
                .toUriString();

        dto.id = fileStoredInDataBase.getFileId();
        dto.name = fileStoredInDataBase.getName();
        dto.type = fileStoredInDataBase.getType();
        dto.url = fileDownloadUri;
        dto.size = fileStoredInDataBase.getData().length;
        dto.data = fileStoredInDataBase.getData();


        return dto;
    }
}