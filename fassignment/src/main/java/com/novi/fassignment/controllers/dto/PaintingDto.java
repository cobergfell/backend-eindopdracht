package com.novi.fassignment.controllers.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.novi.fassignment.models.FileStoredInDataBase;
import com.novi.fassignment.models.Painting;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

public class PaintingDto

{
    public Long paintingId;
    public String username;
    public String title;
    public String artist;
    public String description;
    public Long creationYear;
    public byte[] image;


    @CreationTimestamp
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+00:00")
    public ZonedDateTime dateTimePosted;
    @UpdateTimestamp
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+00:00")
    public ZonedDateTime lastUpdate;
    public Set<FileStoredInDataBaseDto> attachedFiles = new HashSet<FileStoredInDataBaseDto>();

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

    public Long getCreationYear() {
        return creationYear;
    }

    public void setCreationYear(Long creationYear) {
        this.creationYear = creationYear;
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

    public Set<FileStoredInDataBaseDto> getAttachedFiles() {
        return attachedFiles;
    }

    public void setAttachedFiles(Set<FileStoredInDataBaseDto> attachedFiles) {
        this.attachedFiles = attachedFiles;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
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
        dto.title = painting.getTitle();
        dto.artist = painting.getArtist();
        dto.description = painting.getDescription();
        dto.creationYear = painting.getCreationYear();
        dto.dateTimePosted = painting.getDateTimePosted();
        dto.lastUpdate = painting.getLastUpdate();
        dto.username = painting.getUser().getUsername();

        //How to filter a map, example 1 by converting a list into stream and apply the stream filter method
        //see https://mkyong.com/java8/java-8-streams-filter-examples/
        //List<FileStoredInDataBase> filteredFiles_example1=filesStoredInDataBase.stream()
        //.filter(dbFile -> dbFile.getPainting().getPaintingId().equals(painting.getPaintingId()))
        //.collect(Collectors.toList());

        //How to filter a map, example 2, older method before java 8,  using the list only
        //see https://mkyong.com/java8/java-8-streams-filter-examples/
        //List<FileStoredInDataBase> filteredFiles_example2 = new ArrayList<>();
        //for (FileStoredInDataBase fileStoredInDataBase : filteredFiles_example2) {
        //if (fileStoredInDataBase.getPainting().getPaintingId().equals(painting.getPaintingId())) {
        //filteredFiles_example2.add(fileStoredInDataBase);
        //}
        //}

        //Finally, we do it this way (here, we also directly map the filtered files into the files Dto's
        //List<ResponseFileDto> filteredFiles = filesStoredInDataBase.stream()
        //.filter(dbFile -> dbFile.getPainting().getPaintingId().equals(painting.getPaintingId()))
        //.map(dbFile -> ResponseFileDto.fromFileStoredInDataBase(dbFile))
        //.collect(Collectors.toList());
/*        for (FileStoredInDataBase fileStoredInDataBase : painting.getFiles()) {
            dto.responseFileDtos.add(ResponseFileDto.fromFileStoredInDataBase(fileStoredInDataBase));
        }*/

        for (FileStoredInDataBase fileStoredInDataBase : painting.getFiles()) {
            FileStoredInDataBaseDto responseFileDto=FileStoredInDataBaseDto.fromFileStoredInDataBase(fileStoredInDataBase);
            dto.attachedFiles.add(responseFileDto);
            String type=responseFileDto.getType();
            boolean test=(type.equals("image/png"));
            if (test){dto.image=responseFileDto.getData();}
        }

        return dto;



    }
}