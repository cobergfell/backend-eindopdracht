package com.novi.fassignment.controllers;

import com.novi.fassignment.controllers.dto.PaintingDto;
import com.novi.fassignment.controllers.dto.PaintingInputDto;
import com.novi.fassignment.services.FileStorageInDataBaseServiceImpl;
import com.novi.fassignment.services.PaintingServiceImpl;
import com.novi.fassignment.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

import static com.novi.fassignment.utils.Parsers.myLocalDateTimeParserTypeYearMonthDayHourMinSec;

@RestController

@CrossOrigin("*")
public class PaintingController {

    @Autowired
    private FileStorageInDataBaseServiceImpl storageService;

    @Autowired
    private PaintingServiceImpl paintingService;


    @Autowired
    UserService userService;



    @GetMapping("paintings")
    public ResponseEntity<List<PaintingDto>> getPaintings()
    {    List<PaintingDto> paintingDtos = paintingService.getAllPaintingsByAscId();
        //return ResponseEntity.ok(paintingDtos);
        return new ResponseEntity<List<PaintingDto>>(paintingDtos, HttpStatus.OK);
    }

    @GetMapping("paintings/{id}")
    public ResponseEntity<PaintingDto> getPainting(@PathVariable("id") Long paintingId)
    {    PaintingDto paintingDto = paintingService.getPaintingById(paintingId);
        return new ResponseEntity<PaintingDto>(paintingDto, HttpStatus.OK);
    }


    @GetMapping(value = "paintings/{id}/image")
    public ResponseEntity<byte[]> getImage(@PathVariable("id") Long paintingId) {
        var dto = paintingService.getPaintingById(paintingId);
        byte[] image = dto.getImage();
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
    }


    @PostMapping("paintings")
    public ResponseEntity<Object> initiateDiscussionAboutOnePainting(
            @RequestParam(value="username",required=false) String username,
            @RequestParam(value="title",required=false)  String title,
            @RequestParam(value="artist",required=false) String artist,
            @RequestParam(value="description",required=false)  String description,
            @RequestParam(value="image",required=false)  MultipartFile image,
            @RequestParam(value="files",required=false) MultipartFile[] files,
            @RequestParam(value="audioFiles",required=false) MultipartFile[] audioFiles) {


        String message = "";
        try {
            LocalDateTime dateTimePosted = LocalDateTime.now(ZoneId.of("GMT+00:01"));

            PaintingInputDto inputDto= new PaintingInputDto();
            inputDto.username=username;
            inputDto.title=title;
            inputDto.artist=artist;
            inputDto.description=description;
            inputDto.dateTimePosted=dateTimePosted;
            inputDto.lastUpdate=dateTimePosted;
            inputDto.image=image.getBytes();
            inputDto.files=files;
            inputDto.audioFiles=audioFiles;

            paintingService.createPainting(inputDto);
            message = "Painting submitted!";
            return new ResponseEntity<Object>(inputDto, HttpStatus.CREATED);

        } catch (Exception exception) {
            message = "Painting could not be submitted/uploaded!";
            return new ResponseEntity<>(exception.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping("paintings/{id}")
    public ResponseEntity<Object> updatePaintingWithFiles(@PathVariable("id") Long paintingId,
                                                          @RequestParam(value="username",required=false) String username,
                                                          @RequestParam(value="dateTimePosted",required=false) String dateTimePosted,
                                                          @RequestParam(value="title",required=false)  String title,
                                                          @RequestParam(value="artist",required=false) String artist,
                                                          @RequestParam(value="description",required=false)  String description,
                                                          @RequestParam(value="image",required=false)  MultipartFile image,
                                                          @RequestParam(value="files",required=false) MultipartFile[] files,
                                                          @RequestParam(value="audioFiles",required=false) MultipartFile[] audioFiles) {

        if (description.equals("")){return new ResponseEntity<>("Error: request was incomplete, description should be filled in",HttpStatus.BAD_REQUEST); }


        try {
            LocalDateTime localDateTimePosted = myLocalDateTimeParserTypeYearMonthDayHourMinSec(dateTimePosted);
            LocalDateTime lastUpdate = LocalDateTime.now(ZoneId.of("GMT+00:01"));
            ZonedDateTime zonedLastUpdate = lastUpdate.atZone(ZoneId.of("GMT+00:01"));

            PaintingInputDto inputDto = new PaintingInputDto();
            inputDto.paintingId = paintingId;
            inputDto.username = username;
            inputDto.title = title;
            inputDto.artist = artist;
            inputDto.description = description;
            if (image != null){inputDto.image=image.getBytes();}
            else {inputDto.image=null;}
            inputDto.dateTimePosted = localDateTimePosted;
            inputDto.lastUpdate = lastUpdate;

            inputDto.files = files;
            inputDto.audioFiles = audioFiles;

            paintingService.updatePainting(inputDto);
            return new ResponseEntity<Object>(inputDto, HttpStatus.CREATED);

        } catch (Exception exception) {
            return new ResponseEntity<Object>("Project could not be updated!", HttpStatus.BAD_REQUEST);
        }


    }



    @DeleteMapping("paintings/{id}")
    public ResponseEntity<HttpStatus> deletePainting(@PathVariable("id") long id) {
        try {
            paintingService.deletePaintingById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("paintings")
    public ResponseEntity<HttpStatus> deleteAllPaintings() {
        try {
            paintingService.deleteAllPaintings();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
