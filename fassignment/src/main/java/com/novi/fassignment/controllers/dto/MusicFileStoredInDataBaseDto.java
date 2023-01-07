package com.novi.fassignment.controllers.dto;

import com.novi.fassignment.models.FileStoredInDataBase;
import com.novi.fassignment.models.MusicFileStoredInDataBase;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.persistence.Lob;
import java.net.URI;


public class MusicFileStoredInDataBaseDto {
    private Long id;
    private String name;
    private String type;
    private int size;
    private String bytesInDatabaseUrl;//url to bytes
    private String fileOnDiskUrl;// url of  complete file stored on disc
    private long fileOnDiskId;// Id of complete file stored on disc
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

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getBytesInDatabaseUrl() {
        return bytesInDatabaseUrl;
    }

    public void setBytesInDatabaseUrl(String bytesInDatabaseUrl) {
        this.bytesInDatabaseUrl = bytesInDatabaseUrl;
    }

    public String getFileOnDiskUrl() {
        return fileOnDiskUrl;
    }

    public void setFileOnDiskUrl(String fileOnDiskUrl) {
        this.fileOnDiskUrl = fileOnDiskUrl;
    }

    public long getFileOnDiskId() {
        return fileOnDiskId;
    }

    public void setFileOnDiskId(long fileOnDiskId) {
        this.fileOnDiskId = fileOnDiskId;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public static MusicFileStoredInDataBaseDto fromFileStoredInDataBase(MusicFileStoredInDataBase fileStoredInDataBase) {

        var dto = new MusicFileStoredInDataBaseDto();
        String fileDownloadUri = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/filesOnDisk/")
                .path(String.valueOf(fileStoredInDataBase.getFileId()))
                .toUriString();

        dto.id = fileStoredInDataBase.getFileId();
        dto.name = fileStoredInDataBase.getName();
        dto.type = fileStoredInDataBase.getType();
        dto.bytesInDatabaseUrl = fileDownloadUri;
        dto.size = fileStoredInDataBase.getData().length;
        dto.data = fileStoredInDataBase.getData();
        //dto.fileOnDiskUrl = fileStoredInDataBase.getFile_on_disk_url();
        dto.fileOnDiskUrl = fileStoredInDataBase.getFileOnDiskUrl();
        dto.fileOnDiskId = fileStoredInDataBase.getFileOnDiskId();

        return dto;
    }
}