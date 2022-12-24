package com.novi.fassignment.controllers;

import com.novi.fassignment.controllers.dto.*;
import com.novi.fassignment.models.*;
import com.novi.fassignment.repositories.QuestionRepository;
import com.novi.fassignment.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.novi.fassignment.utils.Parsers.myLocalDateTimeParserTypeYearMonthDayHourMinSec;

@RestController
@CrossOrigin("*")
public class QuestionController {


    @Autowired
    private QuestionServiceImpl questionService;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    UserService userService;

    @Autowired
    private PaintingServiceImpl paintingService;


    @GetMapping("questions")
    public ResponseEntity<List<QuestionDto>> getQuestions()
    {    List<QuestionDto> questionDtos = questionService.getAllQuestions();
        return ResponseEntity.ok(questionDtos);
    }

    @GetMapping("questions/{questionId}")
    public ResponseEntity<QuestionDto> getQuestion(@PathVariable("questionId") Long questionId)
    {    QuestionDto questionDto = questionService.getQuestionById(questionId);
        return ResponseEntity.ok(questionDto);
    }

    @GetMapping("questions/byPainting/{id}")
    public ResponseEntity<List<QuestionDto>> getQuestionsByPaintingId(@PathVariable("id") Long paintingId)
    {    List<QuestionDto> questionDtos = questionService.getQuestionsByPaintingId(paintingId);
        return ResponseEntity.ok(questionDtos);
    }

    @GetMapping(value = "questions/{id}/image")
    public ResponseEntity<byte[]> getImage(@PathVariable("id") Long id) {
        var dto = questionService.getQuestionById(id);
        byte[] image = dto.getImage();
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
    }

    @PostMapping("questions/{id}")//id is the id of the painting about which the question is asked
    public ResponseEntity<Object> sendPainting(
            @PathVariable("id") Long id,
            @RequestParam("username") String username,
            @RequestParam(value="title",required=false)  String title,
            @RequestParam(value="content",required=false)  String content,
            @RequestParam(value="image",required=false)  MultipartFile image,
            @RequestParam(value="files",required=false) MultipartFile[] files,
            @RequestParam(value="musicFiles",required=false) MultipartFile[] musicFiles) {


        String message = "";
        try {
            LocalDateTime dateTimePosted = LocalDateTime.now(ZoneId.of("GMT+00:01"));
            QuestionInputDto inputDto= new QuestionInputDto();
            inputDto.idRelatedItem=id;
            inputDto.username=username;
            inputDto.dateTimePosted=dateTimePosted;
            inputDto.lastUpdate=dateTimePosted;
            if (title != null){inputDto.title=title;}
            else{inputDto.title=null;}
            if (content != null){inputDto.content=content;}
            else{inputDto.content=null;}
            if (image != null){inputDto.image=image.getBytes();}
            else{inputDto.image=null;}
            if (files != null){inputDto.files=files;}
            else{inputDto.files=null;}
            if (musicFiles != null){inputDto.musicFiles=musicFiles;}
            else{inputDto.musicFiles=null;}


            questionService.createQuestion(inputDto);
            message = "question submitted!";
            //return ResponseEntity.status(HttpStatus.CREATED).build();
            return new ResponseEntity<Object>(inputDto, HttpStatus.CREATED);

        } catch (Exception exception) {
            message = "Question could not be submitted/uploaded!";
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("questions/{id}")
    public ResponseEntity<Object> updatePaintingWithFiles(@PathVariable("id") Long questionId,
                                                          @RequestParam(value="username",required=false) String username,
                                                          @RequestParam(value="dateTimePosted",required=false) String dateTimePosted,
                                                          @RequestParam(value="title",required=false)  String title,
                                                          @RequestParam(value="content",required=false)  String content,
                                                          @RequestParam(value="image",required=false)  MultipartFile image,
                                                          @RequestParam(value="files",required=false) MultipartFile[] multipartFiles,
                                                          @RequestParam(value="musicFiles",required=false) MultipartFile[] musicFiles) {
        String message_painting = "";
        Optional<Question> currentQuestion = questionRepository.findById(questionId);

        if (currentQuestion.isPresent()) {
            Question questionToUpdate = currentQuestion.get();
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
                        questionToUpdate.setUser(userFromCustomUser);
                    }

                    QuestionInputDto inputDto= new QuestionInputDto();
                    inputDto.questionId=questionId;
                    inputDto.username=username;
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

                    questionService.updateQuestion(inputDto, questionToUpdate);

                    message = "Question submitted!";
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

    @DeleteMapping("questions/{id}")
    public ResponseEntity<HttpStatus> deleteQuestion(@PathVariable("id") long id) {
        try {
            questionService.deleteQuestionById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("questions")
    public ResponseEntity<HttpStatus> deleteAllQuestions() {
        try {
            questionService.deleteAllQuestions();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}