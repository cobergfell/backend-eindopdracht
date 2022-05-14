package com.novi.fassignment.controllers;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.novi.fassignment.controllers.dto.*;
import com.novi.fassignment.models.*;
import com.novi.fassignment.repositories.QuestionRepository;
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
import java.util.Set;

import static com.novi.fassignment.utils.Parsers.myLocalDateTimeParserTypeYearMonthDayHourMinSec;

@RestController
//@CrossOrigin("http://localhost:8080")
@CrossOrigin("*")
public class QuestionController {

    //@Autowired
    //private FileStorageInDataBaseService storageService;

    @Autowired
    private QuestionServiceImpl questionService;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    UserService userService;

    @Autowired
    private PaintingServiceImpl paintingService;

    @GetMapping("api/user/questions")
    public List<QuestionDto> getQuestions() {
        var dtos = new ArrayList<QuestionDto>();
        var questions = questionService.getAllQuestions();
        //List<FileStoredInDataBase> filesStoredInDataBase = storageService.getAllFilesAsList();

        for (Question question : questions) {
            //questionService.getFiles(question);
            dtos.add(QuestionDto.fromQuestionToDto(question));
        }
        return dtos;
    }

    @GetMapping("api/user/questions/{questionId}")
    public QuestionDto getQuestion(@PathVariable("questionId") Long questionId) {
        var dto = new QuestionDto();
        var question = questionService.getQuestionById(questionId);
        dto=QuestionDto.fromQuestionToDto(question);
        return dto;
    }

    @GetMapping("api/user/questions-by-paintingId/{paintingId}")
    public List<QuestionDto>  getQuestionsByPaintingId(@PathVariable("paintingId") Long paintingId) {
        var dtos = new ArrayList<QuestionDto>();
        var painting = paintingService.getPaintingById(paintingId);
        var questions= painting.getQuestions();
        for (Question question : questions) {
            //answerService.getFiles(answer);
            dtos.add(QuestionDto.fromQuestionToDto(question));
        }
        return dtos;
    }



    @DeleteMapping("api/user/questions/{questionId}")
    public ResponseEntity<HttpStatus> deleteQuestion(@PathVariable("questionId") long questionId) {
        try {
            questionService.deleteQuestionById(questionId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("api/user/questions")
    public ResponseEntity<HttpStatus> deleteAllQuestions() {
        try {
            questionService.deleteAllQuestions();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("api/user/question-upload/{id}")//id is the id of the project about which the question is asked
    public ResponseEntity<Object> sendPainting(
            @PathVariable("id") Long id,
            @RequestParam("username") String username,
            @RequestParam("title")  String title,
            @RequestParam("content")  String content,
            //@RequestParam("questionRelatedTo")  String questionRelatedTo,
            @RequestParam("image")  MultipartFile image,
            @RequestParam(value="files",required=false) MultipartFile[] files,
            @RequestParam(value="musicFiles",required=false) MultipartFile[] musicFiles) {


        String message = "";
        try {
            LocalDateTime dateTimePosted = LocalDateTime.now(ZoneId.of("GMT+00:01"));
//            ZonedDateTime zonedDateTimePosted = dateTimePosted.atZone(ZoneId.of("GMT+00:01"));
//            DateTimeFormatter formatter = DateTimeFormatter.ISO_ZONED_DATE_TIME;
//            formatter.format(zonedDateTimePosted);

            QuestionInputDto inputDto= new QuestionInputDto();
            inputDto.idRelatedItem=id;
            inputDto.username=username;
            inputDto.title=title;
            inputDto.content=content;
            inputDto.dateTimePosted=dateTimePosted;
            inputDto.lastUpdate=dateTimePosted;
            inputDto.image=image.getBytes();
            inputDto.files=files;
            inputDto.musicFiles=musicFiles;

            questionService.createQuestion(inputDto);
            message = "question submitted!";
            return ResponseEntity.noContent().build();

        } catch (Exception exception) {
            message = "Question could not be submitted/uploaded!";
            return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
        }
    }

    @PostMapping("api/user/update-question/{id}")
    public ResponseEntity<Object> updatePaintingWithFiles(@PathVariable("questionId") Long questionId,
                                                          @RequestParam(value="username",required=false) String username,
                                                          @RequestParam(value="dateTimePosted",required=false) String dateTimePosted,
                                                          @RequestParam(value="title",required=false)  String title,
                                                          @RequestParam(value="content",required=false)  String content,
                                                          @RequestParam(value="image",required=false)  MultipartFile image,
                                                          @RequestParam(value="files",required=false) MultipartFile[] multipartFiles,
                                                          @RequestParam(value="musicFiles",required=false) MultipartFile[] musicFiles) {
        String message_painting = "";
        Optional<Question> currentQuestion = questionRepository.findById(questionId);
//        System.out.println( formatter.format(zdt) );

        if (currentQuestion.isPresent()) {
            //paintingService.deletePaintingById(id);
            Question questionToUpdate = currentQuestion.get();
            ///paintingService.createPainting(updatedPainting);
            try {
                //LocalDate datePosted = LocalDate.now(ZoneId.of("GMT+00:00"));
                String message = "";
                try {
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

/*    @PostMapping("api/user/create-or-update-question/{id}")
    public ResponseEntity<Object> createOrUpdateQuestion(
            @PathVariable("id") Long id,
            @RequestParam("username") String username,
            @RequestParam("title")  String title,
            @RequestParam("content") String content,
            @RequestParam("questionRelatedTo")  String questionRelatedTo,
            @RequestParam(value="image",required=false)  MultipartFile image,
            @RequestParam(value="files",required=false) MultipartFile[] multipartFiles,
            @RequestParam(value="musicFiles",required=false) MultipartFile[] musicFiles) {
        String message = "";
        LocalDateTime dateTimePosted = LocalDateTime.now(ZoneId.of("GMT+00:00"));
        //ZonedDateTime zonedDateTimePosted = dateTimePosted.atZone(ZoneId.of("GMT+00:00"));

        Question question = new Question();
        Painting painting = new Painting();
        if (questionRelatedTo.equals("painting")) {
            try {
                painting = paintingService.getPaintingById(id);
            } catch (Exception exception) {
                message = "Painting Id not found";
                id = Long.valueOf(-1);
            }
        } else if (questionRelatedTo.equals("question")) {   //in that case we edit question
                try {
                    question =questionService.getQuestionById(id);

                } catch (Exception exception) {
                    message = "question Id not found";
                    id = Long.valueOf(-1);
                }

        } else {
            id = Long.valueOf(-1);
        }
        try {
            QuestionInputDto inputDto = new QuestionInputDto();
            inputDto.username = username;
            inputDto.title = title;
            inputDto.content = content;
            inputDto.dateTimePosted = dateTimePosted;
            inputDto.lastUpdate = dateTimePosted;
            inputDto.questionRelatedTo = questionRelatedTo;
            inputDto.idRelatedItem = id;
            inputDto.files = multipartFiles;

            if (questionRelatedTo.equals("question")){//in that case we edit question
                inputDto.questionId = id;
                questionService.updateQuestion(inputDto,question);
            }
            else{questionService.createQuestion(inputDto);}


            message = "Question submitted!";
            return ResponseEntity.noContent().build();
        } catch (Exception exception) {
            message = "Question could not be submitted";
            return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
        }

    }*/

}