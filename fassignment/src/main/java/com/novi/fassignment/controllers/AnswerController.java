package com.novi.fassignment.controllers;

import com.novi.fassignment.controllers.dto.AnswerDto;
import com.novi.fassignment.controllers.dto.AnswerInputDto;
import com.novi.fassignment.models.Answer;
import com.novi.fassignment.models.User;
import com.novi.fassignment.repositories.AnswerRepository;
import com.novi.fassignment.services.QuestionServiceImpl;
import com.novi.fassignment.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.novi.fassignment.services.AnswerService;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import static com.novi.fassignment.utils.Parsers.myLocalDateTimeParserTypeYearMonthDayHourMinSec;


@RestController
//@CrossOrigin("http://localhost:8080")
@CrossOrigin("*")
public class AnswerController {
    @Autowired
    private AnswerService answerService;

    @Autowired
    QuestionServiceImpl questionService;

    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    UserService userService;

    @GetMapping("answers")
    public ResponseEntity<List<AnswerDto>> getAnswers()
    {    List<AnswerDto> answersDtos = answerService.getAllAnswers();
        return ResponseEntity.ok(answersDtos);
    }

    @GetMapping("answers/{id}")
    public ResponseEntity<AnswerDto> getAnswerById(@PathVariable("id") Long answerId)
    {    AnswerDto answerDto = answerService.getAnswerById(answerId);
        return ResponseEntity.ok(answerDto);
    }

    @GetMapping("answers/byQuestion/{id}")
    public ResponseEntity<List<AnswerDto>> getAnswersByQuestionId(@PathVariable("id") Long questionId)
    {    List<AnswerDto> answerDtos = answerService.getAnswersByQuestionId(questionId);
        return ResponseEntity.ok(answerDtos);
    }

    @DeleteMapping("answers/{id}")
    public ResponseEntity<HttpStatus> deleteAnswer(@PathVariable("id") long answerId) {
        try {
            answerService.deleteAnswerById(answerId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("answers")
    public ResponseEntity<HttpStatus> deleteAllAnswers() {
        try {
            answerService.deleteAllAnswers();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("answers/{id}")
    public ResponseEntity<Object> sendAnswer(
            @PathVariable("id") Long questionId,
            @RequestParam("username") String username,
            @RequestParam(value="title",required=false)  String title,
            @RequestParam(value="content",required=false)  String content,
            @RequestParam(value="image",required=false) MultipartFile image,
            @RequestParam(value="files",required=false) MultipartFile[] files,
            @RequestParam(value="musicFiles",required=false) MultipartFile[] musicFiles) {

        try {
            if (content.equals("")){return new ResponseEntity<>("Error: request was incomplete, answer content should be filled in, please go back to painting page",HttpStatus.BAD_REQUEST); }
            LocalDateTime dateTimePosted = LocalDateTime.now(ZoneId.of("GMT+00:01"));
            AnswerInputDto inputDto= new AnswerInputDto();
            inputDto.idRelatedItem = questionId;
            inputDto.username=username;
            inputDto.dateTimePosted=dateTimePosted;
            inputDto.lastUpdate=dateTimePosted;
            inputDto.title=title;
            inputDto.content=content;
            if (image != null){inputDto.image=image.getBytes();}
            else{inputDto.image=null;}
            inputDto.files=files;
            inputDto.musicFiles=musicFiles;

            answerService.createAnswer(inputDto);
            return new ResponseEntity<Object>(inputDto, HttpStatus.CREATED);


        } catch (Exception exception) {
            return new ResponseEntity<>("Answer could not be submitted/uploaded!",HttpStatus.BAD_REQUEST);
        }
    }


    @PutMapping("answers/{id}")
    public ResponseEntity<Object> updateAnswer(@PathVariable("id") Long answerId,
                                                          @RequestParam(value="username",required=false) String username,
                                                          @RequestParam(value="dateTimePosted",required=false) String dateTimePosted,
                                                          @RequestParam(value="title",required=false)  String title,
                                                          @RequestParam(value="content",required=false)  String content,
                                                          @RequestParam("idRelatedItem")  Long questionId,
                                                          @RequestParam(value="image",required=false)  MultipartFile image,
                                                          @RequestParam(value="files",required=false) MultipartFile[] files,
                                                          @RequestParam(value="musicFiles",required=false) MultipartFile[] musicFiles) {
        String message_painting = "";
        Optional<Answer> currentAnswer = answerRepository.findById(answerId);


        if (currentAnswer.isPresent()) {
            Answer answerToUpdate = currentAnswer.get();
            try {
                String message = "";
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
                    answerToUpdate.setUser(userFromCustomUser);
                }

                AnswerInputDto inputDto= new AnswerInputDto();
                inputDto.answerId=answerId;
                inputDto.username=username;
                inputDto.idRelatedItem = questionId;
                inputDto.title=title;
                inputDto.content=content;
                if (image != null){inputDto.image=image.getBytes();}
                else{inputDto.image=null;}
                inputDto.files=files;
                inputDto.musicFiles=musicFiles;

                answerService.updateAnswer(inputDto, answerToUpdate);

                return new ResponseEntity<Object>(inputDto, HttpStatus.CREATED);


            } catch (Exception exception) {
                return new ResponseEntity<Object>("Answer could not be submitted/uploaded!",HttpStatus.BAD_REQUEST);
            }


        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
