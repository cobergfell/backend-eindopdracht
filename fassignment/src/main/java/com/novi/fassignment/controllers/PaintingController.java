package com.novi.fassignment.controllers;

import com.novi.fassignment.controllers.dto.PaintingDto;
import com.novi.fassignment.controllers.dto.PaintingInputDto;
import com.novi.fassignment.models.FileStoredInDataBase;
import com.novi.fassignment.models.Painting;
import com.novi.fassignment.models.User;
import com.novi.fassignment.repositories.PaintingRepository;
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
    private PaintingRepository paintingRepository;

    @Autowired
    UserService userService;


/*    @GetMapping("api/user/paintings")
    public List<PaintingDto> getPaintings() {
        var dtos = new ArrayList<PaintingDto>();
        //var paintings = paintingService.getAllPaintings();
        var paintings = paintingService.getAllPaintingsByAscId();
        List<FileStoredInDataBase> filesStoredInDataBase = storageService.getAllFilesAsList();

        for (Painting painting : paintings) {
            //paintingService.getFiles(painting);
            dtos.add(PaintingDto.fromPaintingToDto(painting));
        }
        return dtos;
    }

    @GetMapping("api/user/paintings/{paintingId}")
    public PaintingDto getPainting(@PathVariable("paintingId") Long paintingId) {
        var dto = new PaintingDto();
        var painting = paintingService.getPaintingById(paintingId);
        dto=PaintingDto.fromPaintingToDto(painting);
        return dto;
    }*/



    @GetMapping("api/user/paintings")
    public ResponseEntity<List<PaintingDto>> getPaintings()
    {    List<PaintingDto> paintingDtos = paintingService.getAllPaintingsByAscId();
        return ResponseEntity.ok(paintingDtos);
    }

    @GetMapping("api/user/paintings/{paintingId}")
    public ResponseEntity<PaintingDto> getPainting(@PathVariable("paintingId") Long paintingId)
    {    PaintingDto paintingDto = paintingService.getPaintingById(paintingId);
        return ResponseEntity.ok(paintingDto);
    }


    @GetMapping(value = "api/user/paintings/image/{paintingId}")
    public ResponseEntity<byte[]> getImage(@PathVariable("paintingId") Long paintingId) {
        var dto = paintingService.getPaintingById(paintingId);
        byte[] image = dto.getImage();
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
    }


    @PostMapping("api/user/paintings-upload")
    public ResponseEntity<Object> sendPainting(
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
            return ResponseEntity.noContent().build();

        } catch (Exception exception) {
            message = "Painting could not be submitted/uploaded!";
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping("api/user/paintings-update/{paintingId}")
    public ResponseEntity<Object> updatePaintingWithFiles(@PathVariable("paintingId") Long paintingId,
                                                          @RequestParam(value="username",required=false) String username,
                                                          @RequestParam(value="dateTimePosted",required=false) String dateTimePosted,
                                                          @RequestParam(value="title",required=false)  String title,
                                                          @RequestParam(value="artist",required=false) String artist,
                                                          @RequestParam(value="description",required=false)  String description,
                                                          @RequestParam(value="image",required=false)  MultipartFile image,
                                                          @RequestParam(value="files",required=false) MultipartFile[] multipartFiles,
                                                          @RequestParam(value="audioFiles",required=false) MultipartFile[] audioFiles) {
        String message_painting = "";
        Optional<Painting> currentPainting = paintingRepository.findById(paintingId);

        if (currentPainting.isPresent()) {
            Painting paintingToUpdate = currentPainting.get();

            try {
                String message = "";
                try {


                    LocalDateTime localDateTimePosted =myLocalDateTimeParserTypeYearMonthDayHourMinSec(dateTimePosted);
                    LocalDateTime lastUpdate = LocalDateTime.now(ZoneId.of("GMT+00:01"));
                    ZonedDateTime zonedLastUpdate = lastUpdate.atZone(ZoneId.of("GMT+00:01"));

                    if (username != null){
                        Optional<User> user = userService.getUser(username);
                        String password = user.get().getPassword();
                        String email = user.get().getEmail();
                        User userFromCustomUser = new User();
                        userFromCustomUser.setUsername(username);
                        userFromCustomUser.setPassword(password);
                        userFromCustomUser.setEmail(email);
                        paintingToUpdate.setUser(userFromCustomUser);
                    }

                    PaintingInputDto inputDto= new PaintingInputDto();
                    inputDto.paintingId=paintingId;
                    inputDto.username=username;
                    inputDto.dateTimePosted=localDateTimePosted;
                    inputDto.lastUpdate=lastUpdate;
                    if (title != null){inputDto.title=title;}
                    else{inputDto.title=paintingToUpdate.getTitle();}
                    if (artist != null){inputDto.artist=artist;}
                    else{inputDto.artist=paintingToUpdate.getArtist();}
                    if (description != null){inputDto.description=description;}
                    else{inputDto.description=paintingToUpdate.getDescription();}
                    if (image != null){inputDto.image=image.getBytes();}
                    else{inputDto.image=paintingToUpdate.getImage();}
                    if (multipartFiles != null){inputDto.files=multipartFiles;}
                    else{inputDto.files=null;}
                    if (audioFiles != null){inputDto.audioFiles=audioFiles;}
                    else{inputDto.audioFiles=null;}
                    inputDto.questions=paintingToUpdate.getQuestions();
                    inputDto.answers=paintingToUpdate.getAnswers();

                    paintingService.updatePainting(inputDto, paintingToUpdate);

                    message = "Painting submitted!";
                    return ResponseEntity.noContent().build();

                } catch (Exception exception) {
                    message = "Painting could not be submitted/uploaded!";
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }


            } catch (Exception exception) {
                message_painting = "Painting could not be submitted/uploaded!";
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }


        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }



    @DeleteMapping("api/user/paintings/delete/{paintingId}")
    public ResponseEntity<HttpStatus> deletePainting(@PathVariable("paintingId") long paintingId) {
        try {
            paintingService.deletePaintingById(paintingId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("api/user/paintings/delete")
    public ResponseEntity<HttpStatus> deleteAllPaintings() {
        try {
            paintingService.deleteAllPaintings();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
