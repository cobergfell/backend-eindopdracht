package com.novi.fassignment.controllers;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.novi.fassignment.controllers.dto.AnswerDto;
import com.novi.fassignment.controllers.dto.AnswerInputDto;
import com.novi.fassignment.controllers.dto.QuestionDto;
import com.novi.fassignment.controllers.dto.QuestionInputDto;
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

@RestController
//@CrossOrigin("http://localhost:8080")
@CrossOrigin("*")
public class QuestionController {

    @Autowired
    private FileStorageInDataBaseService storageService;

    @Autowired
    private QuestionServiceImpl questionService;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    UserService userService;

    @Autowired
    private PaintingServiceImpl paintingService;

/*
    @Autowired
    private QuestionGetDto questionGetDto;//Dto are usually static methods but in this case it had to be instantiated to instantiate services
*/

/*//@PostMapping("api/user/questions-unrelated-to-items-without-files/{id}")
@PostMapping("api/user/questions-unrelated-to-items-without-files")
public ResponseEntity<Object> sendQuestionUnrelatedToItemsWithoutAttachment(
        //@PathVariable("id") Long id,
        @RequestParam("username") String username,
        @RequestParam("title")  String title,
        @RequestParam("content") String content,
        //@RequestParam("questionRelatedTo")  String questionRelatedTo,
        @RequestParam("tags")  String tags) {
    String questionRelatedTo="";
    String message = "";
    try {
        //LocalDate dateTimePosted = LocalDate.now(ZoneId.of("GMT+00:00"));
        LocalDateTime dateTimePosted = LocalDateTime.now(ZoneId.of("GMT+00:00"));
        ZonedDateTime zonedDateTimePosted = dateTimePosted.atZone(ZoneId.of("GMT+00:00"));
        Question question = new Question();
//        Painting painting = new Painting();
//        MusicPiece musicPiece = new MusicPiece();
//        if(questionRelatedTo.equals("painting")){painting = paintingService.getPaintingById(id);}
//        else {musicPiece = musicPieceService.getMusicPieceById(id);}
        Optional<User> user=userService.getUser(username);
        String password = user.get().getPassword();
        String email = user.get().getEmail();
        User userFromCustomUser = new User();
        userFromCustomUser.setUsername(username);
        userFromCustomUser.setPassword(password);
        userFromCustomUser.setEmail(email);
        question.setUser(userFromCustomUser);
        question.setTitle(title);
        question.setDateTimePosted(zonedDateTimePosted);
        question.setLastUpdate(zonedDateTimePosted);
        question.setContent(content);
        question.setTags(tags);
//        if(questionRelatedTo.equals("painting")){question.setPainting(painting);}
//        else{question.setMusicPiece(musicPiece);}
        question.setPainting(null);
        question.setMusicPiece(null);
        questionService.createQuestionWithoutAttachment(question);
        message = "Question submitted!";
        return ResponseEntity.noContent().build();

    } catch (Exception exception) {
        message = "Question could not be submitted/uploaded!";
        return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
    }
}*/


/*    @PostMapping("api/user/questions-related-to-items-without-files/{id}")
    public ResponseEntity<Object> sendQuestionRelatedToItemsWithoutAttachment(
            @PathVariable("id") Long id,
            @RequestParam("username") String username,
            @RequestParam("title")  String title,
            @RequestParam("content") String content,
            @RequestParam("questionRelatedTo")  String questionRelatedTo,
            @RequestParam("tags")  String tags) {
        String message = "";
        try {
            //LocalDate dateTimePosted = LocalDate.now(ZoneId.of("GMT+00:00"));
            LocalDateTime dateTimePosted = LocalDateTime.now(ZoneId.of("GMT+00:00"));
            ZonedDateTime zonedDateTimePosted = dateTimePosted.atZone(ZoneId.of("GMT+00:00"));
            Question question = new Question();
            Painting painting = new Painting();
            MusicPiece musicPiece = new MusicPiece();
            if(questionRelatedTo.equals("painting")){painting = paintingService.getPaintingById(id);}
            else {musicPiece = musicPieceService.getMusicPieceById(id);}
            Optional<User> user=userService.getUser(username);
            String password = user.get().getPassword();
            String email = user.get().getEmail();
            User userFromCustomUser = new User();
            userFromCustomUser.setUsername(username);
            userFromCustomUser.setPassword(password);
            userFromCustomUser.setEmail(email);
            question.setUser(userFromCustomUser);
            question.setTitle(title);
            question.setDateTimePosted(zonedDateTimePosted);
            question.setLastUpdate(zonedDateTimePosted);
            question.setContent(content);
            question.setTags(tags);
            if(questionRelatedTo.equals("painting")){question.setPainting(painting);}
            else{question.setMusicPiece(musicPiece);}
            questionService.createQuestionWithoutAttachment(question);
            message = "Question submitted!";
            return ResponseEntity.noContent().build();

        } catch (Exception exception) {
            message = "Question could not be submitted/uploaded!";
            return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
        }
    }*/


/*    //@PostMapping("api/user/questions-related-to-items-with-files-in-database/{id}")
    @PostMapping("api/user/questions-unrelated-to-items-with-files-in-database")
    public ResponseEntity<Object> sendQuestionUnrelatedToItemsWithAttachment(
            //@PathVariable("id") Long id,
            @RequestParam("username") String username,
            @RequestParam("title")  String title,
            @RequestParam("content") String content,
            @RequestParam("tags")  String tags,
            //@RequestParam("questionRelatedTo")  String questionRelatedTo,
            @RequestParam(value="files",required=false) MultipartFile[] files) {
        String questionRelatedTo="";
        String message = "";
        try {
            LocalDateTime dateTimePosted = LocalDateTime.now(ZoneId.of("GMT+00:00"));
            ZonedDateTime zonedDateTimePosted = dateTimePosted.atZone(ZoneId.of("GMT+00:00"));
            Question question = new Question();
            //Painting painting = new Painting();
            //MusicPiece musicPiece = new MusicPiece();
            //if(questionRelatedTo.equals("painting")){painting = paintingService.getPaintingById(id);}
            //else {musicPiece = musicPieceService.getMusicPieceById(id);}
            QuestionInputDto inputDto= new QuestionInputDto();
            inputDto.username=username;
            inputDto.title=title;
            inputDto.content=content;
            inputDto.tags=tags;
            inputDto.dateTimePosted=zonedDateTimePosted;
            inputDto.lastUpdate=zonedDateTimePosted;
            inputDto.questionRelatedTo=questionRelatedTo;
            inputDto.idRelatedItem=Long.valueOf(-1);
            inputDto.files=files;
            question.setPainting(null);
            question.setMusicPiece(null);
            questionService.createQuestion(inputDto);

            message = "Question submitted!";
            return ResponseEntity.noContent().build();

        } catch (Exception exception) {
            message = "Question could not be submitted/uploaded!";
            return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
        }

    }*/

/*
    @PostMapping("api/user/post-question/{id}")
    public ResponseEntity<Object> sendQuestionRelatedToItemsWithAttachment(
            @PathVariable("id") Long id,
            @RequestParam("username") String username,
            @RequestParam("title")  String title,
            @RequestParam("content") String content,
            @RequestParam("tags")  String tags,
            @RequestParam("questionRelatedTo")  String questionRelatedTo,
            @RequestParam(value="files",required=false) MultipartFile[] files) {
        String message = "";
        LocalDateTime dateTimePosted = LocalDateTime.now(ZoneId.of("GMT+00:00"));
        ZonedDateTime zonedDateTimePosted = dateTimePosted.atZone(ZoneId.of("GMT+00:00"));
        Question question = new Question();
        Painting painting = new Painting();
        MusicPiece musicPiece = new MusicPiece();
        if (questionRelatedTo.equals("painting")) {
            try {
                painting = paintingService.getPaintingById(id);
            } catch (Exception exception) {
                message = "Painting Id not found";
                id = Long.valueOf(-1);
            }
        } else if (questionRelatedTo.equals("musicPiece")) {
            try {
                musicPiece = musicPieceService.getMusicPieceById(id);
            } catch (Exception exception) {
                message = "Music piece Id not found";
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
            inputDto.tags = tags;
            inputDto.dateTimePosted = zonedDateTimePosted;
            inputDto.lastUpdate = zonedDateTimePosted;
            inputDto.questionRelatedTo = questionRelatedTo;
            inputDto.idRelatedItem = id;
            inputDto.files = files;

            questionService.createQuestion(inputDto);

            message = "Question submitted!";
            return ResponseEntity.noContent().build();
        } catch (Exception exception) {
            message = "Question could not be submitted";
            return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
        }



    }
*/



/*    @PostMapping("api/user/questions-upload-without-files")
    public ResponseEntity<Object> sendQuestionWithoutAttachment(
            @RequestParam("username") String username,
            @RequestParam("title")  String title,
            @RequestParam("content") String content,
            @RequestParam("tags")  String tags) {
        String message = "";
        try {
            //LocalDate dateTimePosted = LocalDate.now(ZoneId.of("GMT+00:00"));
            LocalDateTime dateTimePosted = LocalDateTime.now(ZoneId.of("GMT+00:00"));
            ZonedDateTime zonedDateTimePosted = dateTimePosted.atZone(ZoneId.of("GMT+00:00"));


            Question question = new Question();
            Optional<User> user=userService.getUser(username);
            String password = user.get().getPassword();
            String email = user.get().getEmail();
            User userFromCustomUser = new User();
            userFromCustomUser.setUsername(username);
            userFromCustomUser.setPassword(password);
            userFromCustomUser.setEmail(email);
            question.setUser(userFromCustomUser);
            question.setTitle(title);
            question.setDateTimePosted(zonedDateTimePosted);
            question.setLastUpdate(zonedDateTimePosted);
            question.setContent(content);
            question.setTags(tags);

            questionService.createQuestionWithoutAttachment(question);
            message = "Question submitted!";
            return ResponseEntity.noContent().build();

        } catch (Exception exception) {
            message = "Question could not be submitted/uploaded!";
            return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
        }

    }*/

/*    @PostMapping("api/user/questions-upload-with-files-in-database")
    public ResponseEntity<Object> sendQuestionImprovedMethod(
            @RequestParam("username") String username,
            @RequestParam("title")  String title,
            @RequestParam("content") String content,
            @RequestParam("tags")  String tags,
            @RequestParam(value="files",required=false) MultipartFile[] files) {
        String message = "";
        try {
            LocalDateTime dateTimePosted = LocalDateTime.now(ZoneId.of("GMT+00:00"));
            ZonedDateTime zonedDateTimePosted = dateTimePosted.atZone(ZoneId.of("GMT+00:00"));

            QuestionInputDto inputDto= new QuestionInputDto();
            inputDto.username=username;
            inputDto.title=title;
            inputDto.content=content;
            inputDto.tags=tags;
            inputDto.dateTimePosted=zonedDateTimePosted;
            inputDto.lastUpdate=zonedDateTimePosted;

            inputDto.files=files;

            questionService.createQuestion(inputDto);
            message = "Question submitted!";
            return ResponseEntity.noContent().build();

        } catch (Exception exception) {
            message = "Question could not be submitted/uploaded!";
            return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
        }
    }*/


    @GetMapping("api/user/questions")
    public List<QuestionDto> getQuestions() {
        var dtos = new ArrayList<QuestionDto>();
        var questions = questionService.getAllQuestions();
        List<FileStoredInDataBase> filesStoredInDataBase = storageService.getAllFilesAsList();

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



    @PostMapping("api/user/create-or-update-question/{id}")
    public ResponseEntity<Object> createOrUpdateQuestion(
            @PathVariable("id") Long id,
            @RequestParam("username") String username,
            @RequestParam("title")  String title,
            @RequestParam("content") String content,
            @RequestParam("tags")  String tags,
            @RequestParam("questionRelatedTo")  String questionRelatedTo,
            @RequestParam(value="files",required=false) MultipartFile[] files) {
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
            inputDto.tags = tags;
            inputDto.dateTimePosted = dateTimePosted;
            inputDto.lastUpdate = dateTimePosted;
            inputDto.questionRelatedTo = questionRelatedTo;
            inputDto.idRelatedItem = id;
            inputDto.files = files;

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

    }

}
/*    @PostMapping("api/user/questions-edit-without-files/{questionId}")
    public ResponseEntity<Object> updateQuestionWithoutFiles(@PathVariable("questionId") long questionId,
       @RequestParam("username") String username,
       @RequestParam("title")  String title,
       @RequestParam("content") String content,
       @RequestParam("tags")  String tags) {
        String message_question = "";

        Optional<Question> currentQuestion = questionRepository.findById(questionId);

        if (currentQuestion.isPresent()) {
            //questionService.deleteQuestionById(id);
            Question updatedQuestion = currentQuestion.get();
            ///questionService.createQuestion(updatedQuestion);
            try {
                //LocalDate datePosted = LocalDate.now(ZoneId.of("GMT+00:00"));
                String message = "";
                try {
                    //LocalDate datePosted = LocalDate.now(ZoneId.of("GMT+00:00"));
                    QuestionInputDto inputDto= new QuestionInputDto();
                    inputDto.username=username;
                    inputDto.title=title;
                    inputDto.content=content;
                    inputDto.tags=tags;
                    inputDto.questionId=questionId;

                    questionService.updateQuestion(inputDto,updatedQuestion);


                    message = "Question submitted!";
                    return ResponseEntity.noContent().build();

                } catch (Exception exception) {
                    message = "Question could not be submitted/uploaded!";
                    return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
                }


            } catch (Exception exception) {
                message_question = "Question could not be submitted/uploaded!";
                return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
            }


        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }*/



/*    @PostMapping("/questions-upload-with-files-in-database")
    public ResponseEntity<Object> sendQuestion(
            @RequestParam("username") String username,
            @RequestParam("title")  String title,
            @RequestParam("body") String body,
            @RequestParam("tags")  String tags,
            //@RequestParam("video") MultipartFile video,
            @RequestParam("files") MultipartFile[] files) {
        String message = "";
        try {
            //LocalDate datePosted = LocalDate.now(ZoneId.of("GMT+00:00"));
            Question question = new Question();
            Optional<User> user=userService.getUser(username);
            String password = user.get().getPassword();
            String email = user.get().getEmail();
            User userFromCustomUser = new User();
            userFromCustomUser.setUsername(username);
            userFromCustomUser.setPassword(password);
            userFromCustomUser.setEmail(email);
            question.setUser(userFromCustomUser);
            question.setTitle(title);
            // question.setDatePosted(datePosted.toString());
            question.setBody(body);
            question.setTags(tags);

            questionService.createQuestion(question);

            List<Question> sortedQuestions=questionService.getAllQuestionsByDescId();
            //Question[] sortedQuestionsArray= (Question[]) sortedQuestions.toArray();//cast array of objects into array of questions
            Question mostRecentQuestion= sortedQuestions.get(0);
            //Question mostRecentQuestion=sortedQuestionsArray[0];
            Long mostRecentQuestionId=mostRecentQuestion.getQuestionId();

            //storageService.store(file);

            List<String> fileNames = new ArrayList<>();

            Arrays.asList(files).stream().forEach(file -> {String message2 = "";
                try {
                    //storageService.store(file);
                    storageService.storeQuestionFile(file,question);

                    fileNames.add(file.getOriginalFilename());
                    message2 = "Uploaded the files successfully: " + fileNames;
                } catch (Exception e) {
                    message2 = "Fail to upload files!";
                }
            });

            Set<FileStoredInDataBase> attachedFiles= new HashSet<>();
            List<FileStoredInDataBase> sortedFiles=storageService.getAllFilesByDescId();
            //FileStoredInDataBase[] sortedFilesArray= (FileStoredInDataBase[]) sortedQuestions.toArray();//cast array of objects into array of questions
            for (int i=0; i<fileNames.size(); i++) {
                FileStoredInDataBase mostRecentFile=sortedFiles.get(i);
                attachedFiles.add(mostRecentFile);
            };

            mostRecentQuestion.setFiles(attachedFiles);

            message = "Question submitted!";
            return ResponseEntity.noContent().build();

        } catch (Exception exception) {
            message = "Question could not be submitted/uploaded!";
            return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
        }

    }*/



