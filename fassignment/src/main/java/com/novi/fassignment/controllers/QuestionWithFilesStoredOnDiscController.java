package com.novi.fassignment.controllers;

import com.novi.fassignment.controllers.dto.QuestionWithFilesStoredOnDiscDto;
import com.novi.fassignment.models.QuestionWithFilesStoredOnDisc;
import com.novi.fassignment.models.User;
import com.novi.fassignment.services.FileUploadService;
import com.novi.fassignment.services.QuestionWithFilesStoredOnDiscServiceImpl;
import com.novi.fassignment.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
//@RequestMapping(value="/questions")// never use this with RequestParam (remember my question to Stackoverflow!)
//@CrossOrigin(origins ="http://localhost:3000")
//@CrossOrigin("http://localhost:8080")
@CrossOrigin(origins ="*")
public class QuestionWithFilesStoredOnDiscController {

    @Autowired
    private QuestionWithFilesStoredOnDiscServiceImpl questionService;

//    @GetMapping("/questions")
//    public List<Question> getAllQuestions() {
//        return questionService.getAllQuestions();
//    }


    @GetMapping("api/user/questions_with_files_stored_on_disc")
    public List<QuestionWithFilesStoredOnDiscDto> getQuestions() {
        var dtos = new ArrayList<QuestionWithFilesStoredOnDiscDto>();
        var questions = questionService.getAllQuestionsWithFilesStoredOnDisc();

        for (QuestionWithFilesStoredOnDisc question : questions) {
            dtos.add(QuestionWithFilesStoredOnDiscDto.fromQuestion(question));
        }

        return dtos;
    }



/*    @GetMapping("/questions")
    public ResponseEntity<List<Question>> getAllQuestions() {
        List<Question> questions = questionService.getAllQuestions();
        return ResponseEntity.ok().body(questions);
    }*/

    @Autowired
    FileUploadService fileUploadService;

    @Autowired
    UserService userService;

/*    @CrossOrigin
    @PostMapping
    public Question save(@RequestBody Question question) {
        return questionService.createQuestion(question);
    }*/


    @PostMapping("api/user/questions_with_files_stored_on_disc")
    public ResponseEntity<Object> sendQuestion(
            @RequestParam("username") String username,
            @RequestParam("title")  String title,
            @RequestParam("content") String content,
            @RequestParam("tags")  String tags,
            //@RequestParam("video") MultipartFile video,
            @RequestParam("audio") MultipartFile audio, RedirectAttributes redirectAttributes) {
            String message = "";
        try {
            //fileUploadService.uploadFile(audio);

            String storageLocationAbsolutePath = fileUploadService.uploadFileAndReturnStorageLocationAbsolutePath(audio);

            redirectAttributes.addFlashAttribute("message",
                    "You successfully uploaded " + audio.getOriginalFilename() + "!");

//            FileUploadService fileUploadService = new FileUploadService();
//            fileUploadService.uploadFile(audio);

            //LocalDate datePosted = LocalDate.now(ZoneId.of("GMT+00:00"));

            QuestionWithFilesStoredOnDisc question = new QuestionWithFilesStoredOnDisc();
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
            question.setContent(content);
            //question.setAudioStoragePath(audio.getOriginalFilename());
            question.setAudioStoragePath(storageLocationAbsolutePath);
            question.setTags(tags);
            Path path = Paths.get(storageLocationAbsolutePath);
            byte[] fileContent = Files.readAllBytes(path);


            Resource resource = new UrlResource(path.toUri());

            if (resource.exists() || resource.isReadable()) {
                question.setAudioStorageUrl(resource.toString());
                message = "Uploaded the file successfully: " + audio.getOriginalFilename();
            } else {
                throw new RuntimeException("Could not read the file!");
            }

            question.setAudioStorage(fileContent);
            question.setAudioStorageName(audio.getOriginalFilename());
            //String extension = FilenameUtils.getExtension(audio.getOriginalFilename());
            //question.setAudioStorageType(extension);
            question.setAudioStorageType(audio.getContentType());

            questionService.createQuestionWithFilesStoredOnDisc(question);
            message = "Question submitted!";
            return ResponseEntity.noContent().build();

        } catch (Exception exception) {
            message = "Question could not be submitted/uploaded!";
            return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
        }
    }

/*    @PostMapping("/questions")
    public ResponseEntity<Object> sendQuestion(
            @RequestParam("title")  String title){
        Question question = new Question();
        question.setTitle(title);
        question.setTitle(title);
        questionService.createQuestion(question);
        return ResponseEntity.ok(question);
    }*/

    @DeleteMapping("api/user/questions_with_files_stored_on_disc/{questionId}")
    public void deleteQuestion(@PathVariable("questionId") long questionId) {
        questionService.deleteQuestionWithFilesStoredOnDisc(questionId);
    }

    @GetMapping("api/user/questions_with_files_stored_on_disc/{questionId}")
    public ResponseEntity<Object> getQuestionById(@PathVariable("questionId") long questionId) {
        QuestionWithFilesStoredOnDisc question = questionService.getQuestionWithFilesStoredOnDiscById(questionId);
        return ResponseEntity.ok(question);
    }

/*    @GetMapping("{id}")
    public User getUserById(@PathVariable("id") long id) {
        userService.getUserById(id);
    }*/


}


/*
then browse to http://localhost:8080/users

or
 in Postman use 'get'
with url:
'localhost:8080/users

you first het []
then POST
{"username":"malabar","firstname":"michel","lastname":"alabar"}
with body raw option JSON*/
