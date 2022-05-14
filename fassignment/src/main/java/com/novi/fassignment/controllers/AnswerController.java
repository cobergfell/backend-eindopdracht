package com.novi.fassignment.controllers;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.novi.fassignment.controllers.dto.AnswerDto;
import com.novi.fassignment.controllers.dto.AnswerInputDto;
import com.novi.fassignment.controllers.dto.QuestionDto;
import com.novi.fassignment.controllers.dto.QuestionInputDto;
import com.novi.fassignment.models.*;
import com.novi.fassignment.repositories.AnswerRepository;
import com.novi.fassignment.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

import static com.novi.fassignment.utils.Parsers.myLocalDateTimeParserTypeYearMonthDayHourMinSec;

@RestController
//@CrossOrigin("http://localhost:8080")
@CrossOrigin("*")
public class AnswerController {

    //@Autowired
    //private FileStorageInDataBaseService storageService;

    @Autowired
    private AnswerServiceImpl answerService;

    @Autowired
    private QuestionServiceImpl questionService;

    @Autowired
    private PaintingServiceImpl paintingService;


    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    UserService userService;
/*
    @Autowired
    private AnswerGetDto answerGetDto;//Dto are usually static methods but in this case it had to be instantiated to instantiate services
*/

    @GetMapping("api/user/answers")
    public List<AnswerDto> getAnswers() {
        var dtos = new ArrayList<AnswerDto>();
        var answers = answerService.getAllAnswers();
        //List<FileStoredInDataBase> filesStoredInDataBase = storageService.getAllFilesAsList();

        for (Answer answer : answers) {
            //answerService.getFiles(answer);
            dtos.add(AnswerDto.fromAnswerToDto(answer));
        }
        return dtos;
    }

    @GetMapping("api/user/answers/{answerId}")
    public AnswerDto getAnswer(@PathVariable("answerId") Long answerId) {
        var dto = new AnswerDto();
        var answer = answerService.getAnswerById(answerId);
        dto=AnswerDto.fromAnswerToDto(answer);
        return dto;
    }

/*    @GetMapping("api/user/answers-by-paintingId/{paintingId}")
    public List<AnswerDto>  getAnswersByPaintingId(@PathVariable("paintingId") Long paintingId) {
        var dtos = new ArrayList<AnswerDto>();
        var painting = paintingService.getPaintingById(paintingId);
        var answers= painting.getAnswers();
        for (Answer answer : answers) {
            //answerService.getFiles(answer);
            dtos.add(AnswerDto.fromAnswerToDto(answer));
        }
        return dtos;
    }*/


    @GetMapping("api/user/answers-by-questionId/{questionId}")
    public List<AnswerDto>  getAnswersByPaintingId(@PathVariable("questionId") Long questionId) {
        var dtos = new ArrayList<AnswerDto>();
        var question = questionService.getQuestionById(questionId);
        var answers= question.getAnswers();
        for (Answer answer : answers) {
            //answerService.getFiles(answer);
            dtos.add(AnswerDto.fromAnswerToDto(answer));
        }
        return dtos;
    }




    @DeleteMapping("api/user/answers/{answerId}")
    public ResponseEntity<HttpStatus> deleteAnswer(@PathVariable("answerId") long answerId) {
        try {
            answerService.deleteAnswerById(answerId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("api/user/answers")
    public ResponseEntity<HttpStatus> deleteAllAnswers() {
        try {
            answerService.deleteAllAnswers();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("api/user/answer-upload/{id}")
    public ResponseEntity<Object> sendPainting(
            @PathVariable("id") Long questionId,
            @RequestParam("username") String username,
            @RequestParam("title")  String title,
            @RequestParam("content")  String content,
            @RequestParam("image")  MultipartFile image,
            @RequestParam(value="files",required=false) MultipartFile[] files,
            @RequestParam(value="musicFiles",required=false) MultipartFile[] musicFiles) {


        String message = "";
        try {
            LocalDateTime dateTimePosted = LocalDateTime.now(ZoneId.of("GMT+00:01"));

            AnswerInputDto inputDto= new AnswerInputDto();
            inputDto.idRelatedItem = questionId;
            inputDto.username=username;
            inputDto.title=title;
            inputDto.content=content;
            inputDto.dateTimePosted=dateTimePosted;
            inputDto.lastUpdate=dateTimePosted;
            inputDto.image=image.getBytes();
            inputDto.files=files;
            inputDto.musicFiles=musicFiles;

            answerService.createAnswer(inputDto);
            message = "question submitted!";
            return ResponseEntity.noContent().build();

        } catch (Exception exception) {
            message = "Question could not be submitted/uploaded!";
            return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
        }
    }


    @PostMapping("api/user/update-answer/{id}")
    public ResponseEntity<Object> updatePaintingWithFiles(@PathVariable("answerId") Long answerId,
                                                          @RequestParam(value="username",required=false) String username,
                                                          @RequestParam(value="dateTimePosted",required=false) String dateTimePosted,
                                                          @RequestParam(value="title",required=false)  String title,
                                                          @RequestParam(value="content",required=false)  String content,
                                                          @RequestParam("idRelatedItem")  Long questionId,
                                                          @RequestParam(value="image",required=false)  MultipartFile image,
                                                          @RequestParam(value="files",required=false) MultipartFile[] multipartFiles,
                                                          @RequestParam(value="musicFiles",required=false) MultipartFile[] musicFiles) {
        String message_painting = "";
        Optional<Answer> currentAnswer = answerRepository.findById(answerId);


        if (currentAnswer.isPresent()) {
            //paintingService.deletePaintingById(id);
            Answer answerToUpdate = currentAnswer.get();
            ///paintingService.createPainting(updatedPainting);
            try {
                //LocalDate datePosted = LocalDate.now(ZoneId.of("GMT+00:00"));
                String message = "";
                try {
                    //LocalDate datePosted = LocalDate.now(ZoneId.of("GMT+00:00"));

//                    var dto = new PaintingDto();
//                    var painting = paintingService.getPaintingById(paintingId);
//                    dto=PaintingDto.fromPaintingToDto(painting);

//                    ZonedDateTime dateTimePostedWithZoneOffset = ZonedDateTime
//                            .parse("2011-12-03T10:15:30+01:00", formatter);
                    //ZonedDateTime dateTimePostedWithZoneOffset = ZonedDateTime
                    //        .parse(dateTimePosted, formatter);

                    LocalDateTime localDateTimePosted =myLocalDateTimeParserTypeYearMonthDayHourMinSec(dateTimePosted);
                    LocalDateTime lastUpdate = LocalDateTime.now(ZoneId.of("GMT+00:01"));
                    ZonedDateTime zonedLastUpdate = lastUpdate.atZone(ZoneId.of("GMT+00:01"));
                    //formatter.format(zonedLastUpdate);


                    if (username != null){
                        Optional<User> user = userService.getUser(username);
                        String password = user.get().getPassword();
                        String email = user.get().getEmail();
                        User userFromCustomUser = new User();
                        userFromCustomUser.setUsername(username);
                        userFromCustomUser.setPassword(password);
                        userFromCustomUser.setEmail(email);
                        answerToUpdate.setUser(userFromCustomUser);
                    }

                    AnswerInputDto inputDto= new AnswerInputDto();
                    inputDto.answerId=answerId;
                    inputDto.username=username;
                    inputDto.idRelatedItem = questionId;
                    if (title != null){inputDto.title=title;}
                    else{inputDto.title=null;}
                    if (content != null){inputDto.content=content;}
                    else{inputDto.content=null;}
                    if (image != null){inputDto.image=image.getBytes();}
                    else{inputDto.image=null;}
                    if (multipartFiles != null){inputDto.files=multipartFiles;}
                    else{inputDto.files=null;}
                    if (musicFiles != null){inputDto.musicFiles=musicFiles;}
                    else{inputDto.musicFiles=null;}


/*                    if (multipartFiles != null){
                        List<String> fileNames = new ArrayList<>();
                        //Set<FileStoredInDataBase> attachedFiles = new HashSet<>();
                        List<MultipartFile> multipartFilesList = new ArrayList<MultipartFile>();
                        Arrays.asList(multipartFiles).stream().forEach(theFile -> multipartFilesList.add(theFile));
                    }
                    if (musicFiles != null){
                        List<String> fileNames = new ArrayList<>();
                        //Set<FileStoredInDataBase> attachedFiles = new HashSet<>();
                        List<MultipartFile> multipartFilesList = new ArrayList<MultipartFile>();
                        Arrays.asList(musicFiles).stream().forEach(theFile -> multipartFilesList.add(theFile));
                    }*/
                    //paintingRepository.save(paintingToUpdate);

                    answerService.updateAnswer(inputDto, answerToUpdate);

                    message = "Question submitted!";
                    return ResponseEntity.noContent().build();

                } catch (Exception exception) {
                    message = "Painting could not be submitted/uploaded!";
                    return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
                }


            } catch (Exception exception) {
                message_painting = "Painting could not be submitted/uploaded!";
                return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
            }


        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

/*    @PostMapping("api/user/create-or-update-answer/{id}")//id must always refer to a question or to an answer for update purposes
    public ResponseEntity<Object> createOrUpdateAnswer(
            @PathVariable("id") Long id,
            @RequestParam("username") String username,
            @RequestParam("title")  String title,
            @RequestParam("content") String content,
            //@RequestParam("tags")  String tags,
            @RequestParam("answerRelatedTo")  String answerRelatedTo,
            @RequestParam(value="files",required=false) MultipartFile[] files) {
        String message = "";
        LocalDateTime dateTimePosted = LocalDateTime.now(ZoneId.of("GMT+00:00"));
        //ZonedDateTime zonedDateTimePosted = dateTimePosted.atZone(ZoneId.of("GMT+00:00"));

        Answer answer = new Answer();
        Question question = new Question();
//        Painting painting = new Painting();
//        MusicPiece musicPiece = new MusicPiece();
        if (answerRelatedTo.equals("question")) {
            try {
                question = questionService.getQuestionById(id);
            } catch (Exception exception) {
                message = "question Id not found";
                id = Long.valueOf(-1);
            }
//        } else if (answerRelatedTo.equals("musicPiece")) {
//            try {
//                musicPiece = musicPieceService.getMusicPieceById(id);
//            } catch (Exception exception) {
//                message = "Music piece Id not found";
//                id = Long.valueOf(-1);
//            }
        } else if (answerRelatedTo.equals("answer")) {   //in that case we edit answer
            try {
                answer =answerService.getAnswerById(id);

            } catch (Exception exception) {
                message = "answer Id not found";
                id = Long.valueOf(-1);
            }

        } else {
            id = Long.valueOf(-1);
        }
        try {
            AnswerInputDto inputDto = new AnswerInputDto();
            inputDto.username = username;
            inputDto.title = title;
            inputDto.content = content;
            //inputDto.tags = tags;
            inputDto.dateTimePosted = dateTimePosted;
            inputDto.lastUpdate = dateTimePosted;
            inputDto.answerRelatedTo = answerRelatedTo;
            inputDto.idRelatedItem = id;
            inputDto.files = files;

            if (answerRelatedTo.equals("answer")){//in that case we edit answer
                inputDto.answerId = id;
                answerService.updateAnswer(inputDto,answer);
            }
            else{answerService.createAnswer(inputDto);}


            message = "Answer submitted!";
            return ResponseEntity.noContent().build();
        } catch (Exception exception) {
            message = "Answer could not be submitted";
            return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
        }

    }*/

}