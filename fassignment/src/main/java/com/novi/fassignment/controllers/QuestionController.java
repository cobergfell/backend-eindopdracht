package com.novi.fassignment.controllers;

import com.novi.fassignment.controllers.dto.*;
import com.novi.fassignment.exceptions.RecordNotFoundException;
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
    public ResponseEntity<Object> sendQuestion(
            @PathVariable("id") Long id,
            @RequestParam("username") String username,
            @RequestParam(value="title")  String title,
            @RequestParam(value="content")  String content,
            @RequestParam(value="image",required=false)  MultipartFile image,
            @RequestParam(value="files",required=false) MultipartFile[] files,
            @RequestParam(value="musicFiles",required=false) MultipartFile[] musicFiles) {

        try {
            if (content.equals("")){return new ResponseEntity<>("Error: request was incomplete, question content should be filled in, please go back to painting page",HttpStatus.BAD_REQUEST); }

            LocalDateTime dateTimePosted = LocalDateTime.now(ZoneId.of("GMT+00:01"));
            QuestionInputDto inputDto= new QuestionInputDto();
            inputDto.idRelatedItem=id;
            inputDto.username=username;
            inputDto.dateTimePosted=dateTimePosted;
            inputDto.lastUpdate=dateTimePosted;
            inputDto.title=title;
            inputDto.content=content;
            if (image != null){inputDto.image=image.getBytes();}
            else{inputDto.image=null;}
            inputDto.files=files;
            inputDto.musicFiles=musicFiles;
            questionService.createQuestion(inputDto);
            return new ResponseEntity<Object>(inputDto, HttpStatus.CREATED);

        } catch (Exception exception) {
            return new ResponseEntity<>(exception.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("questions/{id}")
    public ResponseEntity<Object> updateQuestion(@PathVariable("id") Long questionId,
                                                          @RequestParam(value="username",required=false) String username,
                                                          @RequestParam(value="dateTimePosted",required=false) String dateTimePosted,
                                                          @RequestParam(value="title",required=false)  String title,
                                                          @RequestParam(value="content",required=false)  String content,
                                                          @RequestParam(value="image",required=false)  MultipartFile image,
                                                          @RequestParam(value="files",required=false) MultipartFile[] files,
                                                          @RequestParam(value="musicFiles",required=false) MultipartFile[] musicFiles) {
        //
        try {
            LocalDateTime localDateTimePosted = myLocalDateTimeParserTypeYearMonthDayHourMinSec(dateTimePosted);
            LocalDateTime lastUpdate = LocalDateTime.now(ZoneId.of("GMT+00:01"));
            ZonedDateTime zonedLastUpdate = lastUpdate.atZone(ZoneId.of("GMT+00:01"));

            QuestionInputDto inputDto= new QuestionInputDto();
            inputDto.questionId=questionId;
            inputDto.username = username;
            inputDto.title = title;
            inputDto.content = content;
            if (image != null){inputDto.image=image.getBytes();}
            inputDto.dateTimePosted = localDateTimePosted;
            inputDto.lastUpdate = lastUpdate;
            inputDto.files = files;
            inputDto.musicFiles = musicFiles;

            questionService.updateQuestion(inputDto);
            return new ResponseEntity<Object>(inputDto, HttpStatus.CREATED);

        } catch (Exception exception) {
            return new ResponseEntity<Object>("Answer could not be submitted/uploaded!", HttpStatus.BAD_REQUEST);
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