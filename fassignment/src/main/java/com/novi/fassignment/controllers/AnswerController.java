package com.novi.fassignment.controllers;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.novi.fassignment.controllers.dto.AnswerDto;
import com.novi.fassignment.controllers.dto.AnswerInputDto;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
//@CrossOrigin("http://localhost:8080")
@CrossOrigin("*")
public class AnswerController {

    @Autowired
    private FileStorageInDataBaseService storageService;

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


    @PostMapping("api/user/answers-upload-without-files/{id}")
    public ResponseEntity<Object> sendAnswerWithoutAttachment(
            @PathVariable("id") Long id,
            @RequestParam("username") String username,
            @RequestParam("title")  String title,
            @RequestParam("content") String content,
            @RequestParam("answerRelatedTo")  String answerTo,
            @RequestParam("tags")  String tags) {
        String message = "";
        try {
            //LocalDate dateTimePosted = LocalDate.now(ZoneId.of("GMT+00:00"));
            LocalDateTime dateTimePosted = LocalDateTime.now(ZoneId.of("GMT+00:00"));
            ZonedDateTime zonedDateTimePosted = dateTimePosted.atZone(ZoneId.of("GMT+00:00"));
            Question question = new Question();
            Painting painting = new Painting();
            if(answerTo.equals("question")){question = questionService.getQuestionById(id);}
            else {painting = paintingService.getPaintingById(id);}
            Answer answer = new Answer();
            Optional<User> user=userService.getUser(username);
            String password = user.get().getPassword();
            String email = user.get().getEmail();
            User userFromCustomUser = new User();
            userFromCustomUser.setUsername(username);
            userFromCustomUser.setPassword(password);
            userFromCustomUser.setEmail(email);
            answer.setUser(userFromCustomUser);
            answer.setTitle(title);
            answer.setDateTimePosted(zonedDateTimePosted);
            answer.setLastUpdate(zonedDateTimePosted);
            answer.setContent(content);
            answer.setTags(tags);
            if(answerTo.equals("question")){answer.setQuestion(question);}
            else{answer.setPainting(painting);}
            answerService.createAnswerWithoutAttachment(answer);
            message = "Answer submitted!";
            return ResponseEntity.noContent().build();

        } catch (Exception exception) {
            message = "Answer could not be submitted/uploaded!";
            return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
        }
    }


    @PostMapping("api/user/answers-upload-with-files-in-database/{id}")
    public ResponseEntity<Object> sendAnswerImprovedMethod(
            @PathVariable("id") Long id,
            @RequestParam("username") String username,
            @RequestParam("title")  String title,
            @RequestParam("content") String content,
            @RequestParam("tags")  String tags,
            @RequestParam("answerRelatedTo")  String answerRelatedTo,
            @RequestParam(value="files",required=false) MultipartFile[] files) {
        String message = "";
        try {
            LocalDateTime dateTimePosted = LocalDateTime.now(ZoneId.of("GMT+00:00"));
            ZonedDateTime zonedDateTimePosted = dateTimePosted.atZone(ZoneId.of("GMT+00:00"));
            Question question = new Question();
            Painting painting = new Painting();
            if(answerRelatedTo.equals("question")){question = questionService.getQuestionById(id);}
            else {painting = paintingService.getPaintingById(id);}
            AnswerInputDto inputDto= new AnswerInputDto();
            inputDto.username=username;
            inputDto.title=title;
            inputDto.content=content;
            inputDto.tags=tags;
            inputDto.dateTimePosted=zonedDateTimePosted;
            inputDto.lastUpdate=zonedDateTimePosted;
            inputDto.answerRelatedTo=answerRelatedTo;
            inputDto.idRelatedItem=id;
            //if(answerTo.equals("question")){inputDto.question=question;;}
            //else{inputDto.painting=painting;;}
            inputDto.files=files;

            answerService.createAnswer(inputDto);
            message = "Answer submitted!";
            return ResponseEntity.noContent().build();

        } catch (Exception exception) {
            message = "Answer could not be submitted/uploaded!";
            return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
        }

    }


    @GetMapping("api/user/answers-with-files-in-database")
    public List<AnswerDto> getAnswers() {
        var dtos = new ArrayList<AnswerDto>();
        var answers = answerService.getAllAnswers();
        List<FileStoredInDataBase> filesStoredInDataBase = storageService.getAllFilesAsList();

        for (Answer answer : answers) {
            //answerService.getFiles(answer);
            dtos.add(AnswerDto.fromAnswerToDto(answer));
        }
        return dtos;
    }

    @GetMapping("api/user/answers-with-files-in-database/{answerId}")
    public AnswerDto getAnswer(@PathVariable("answerId") Long answerId) {
        var dto = new AnswerDto();
        var answer = answerService.getAnswerById(answerId);
        dto=AnswerDto.fromAnswerToDto(answer);
        return dto;
    }

    @GetMapping("api/user/answers-by-questionId/{questionId}")
    public List<AnswerDto>  getAnswersByQuestionId(@PathVariable("questionId") Long questionId) {
        var dtos = new ArrayList<AnswerDto>();
        var question = questionService.getQuestionById(questionId);
        var answers= question.getAnswers();
        for (Answer answer : answers) {
            //answerService.getFiles(answer);
            dtos.add(AnswerDto.fromAnswerToDto(answer));
        }
        return dtos;
    }


    @GetMapping("api/user/answers-by-paintingId/{paintingId}")
    public List<AnswerDto>  getAnswersByPaintingId(@PathVariable("paintingId") Long paintingId) {
        var dtos = new ArrayList<AnswerDto>();
        var painting = paintingService.getPaintingById(paintingId);
        var answers= painting.getAnswers();
        for (Answer answer : answers) {
            //answerService.getFiles(answer);
            dtos.add(AnswerDto.fromAnswerToDto(answer));
        }
        return dtos;
    }

    @DeleteMapping("api/user/answers-with-files-in-database/{answerId}")
    public ResponseEntity<HttpStatus> deleteAnswer(@PathVariable("answerId") Long answerId) {
        try {
            answerService.deleteAnswerById(answerId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("api/user/answers-with-files-in-database")
    public ResponseEntity<HttpStatus> deleteAllAnswers() {
        try {
            answerService.deleteAllAnswers();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("api/user/answers-edit-with-files/{answerId}")
    public ResponseEntity<Object> updateAnswerWithFiles(@PathVariable("answerId") Long answerId,
                                                          @RequestParam("username") String username,
                                                          @RequestParam("title")  String title,
                                                          @RequestParam("content") String content,
                                                          @RequestParam("tags")  String tags,
                                                          //@RequestParam("video") MultipartFile video,
                                                          @RequestParam("files") MultipartFile[] files) {
        String message_answer = "";

        Optional<Answer> currentAnswer = answerRepository.findById(answerId);

        if (currentAnswer.isPresent()) {
            //answerService.deleteAnswerById(id);
            Answer updatedAnswer = currentAnswer.get();
            ///answerService.createAnswer(updatedAnswer);
            try {
                //LocalDate datePosted = LocalDate.now(ZoneId.of("GMT+00:00"));
                String message = "";
                try {
                    //LocalDate datePosted = LocalDate.now(ZoneId.of("GMT+00:00"));
                    AnswerInputDto inputDto= new AnswerInputDto();
                    inputDto.username=username;
                    inputDto.title=title;
                    inputDto.content=content;
                    inputDto.tags=tags;
                    inputDto.files=files;
                    inputDto.answerId=answerId;

                    answerService.updateAnswer(inputDto,updatedAnswer);


                    message = "Answer submitted!";
                    return ResponseEntity.noContent().build();

                } catch (Exception exception) {
                    message = "Answer could not be submitted/uploaded!";
                    return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
                }


            } catch (Exception exception) {
                message_answer = "Answer could not be submitted/uploaded!";
                return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
            }


        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("api/user/answers-edit-without-files/{answerId}")
    public ResponseEntity<Object> updateAnswerWithoutFiles(@PathVariable("answerId") Long answerId,
                                                             @RequestParam("username") String username,
                                                             @RequestParam("title")  String title,
                                                             @RequestParam("content") String content,
                                                             @RequestParam("tags")  String tags) {
        String message_answer = "";

        Optional<Answer> currentAnswer = answerRepository.findById(answerId);

        if (currentAnswer.isPresent()) {
            //answerService.deleteAnswerById(id);
            Answer updatedAnswer = currentAnswer.get();
            ///answerService.createAnswer(updatedAnswer);
            try {
                //LocalDate datePosted = LocalDate.now(ZoneId.of("GMT+00:00"));
                String message = "";
                try {
                    //LocalDate datePosted = LocalDate.now(ZoneId.of("GMT+00:00"));
                    AnswerInputDto inputDto= new AnswerInputDto();
                    inputDto.username=username;
                    inputDto.title=title;
                    inputDto.content=content;
                    inputDto.tags=tags;
                    inputDto.answerId=answerId;

                    answerService.updateAnswer(inputDto,updatedAnswer);


                    message = "Answer submitted!";
                    return ResponseEntity.noContent().build();

                } catch (Exception exception) {
                    message = "Answer could not be submitted/uploaded!";
                    return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
                }


            } catch (Exception exception) {
                message_answer = "Answer could not be submitted/uploaded!";
                return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
            }


        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}