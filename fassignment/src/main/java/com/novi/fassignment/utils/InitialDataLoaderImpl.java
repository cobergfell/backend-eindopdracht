package com.novi.fassignment.utils;


import com.novi.fassignment.controllers.dto.AnswerInputDto;
import com.novi.fassignment.controllers.dto.FileStoredInDataBaseInputDto;
import com.novi.fassignment.controllers.dto.PaintingInputDto;
import com.novi.fassignment.controllers.dto.QuestionInputDto;
import com.novi.fassignment.exceptions.StorageException;
import com.novi.fassignment.exceptions.UsernameNotFoundException;
import com.novi.fassignment.models.*;
import com.novi.fassignment.repositories.UserRepository;
import com.novi.fassignment.services.*;
import io.jsonwebtoken.Claims;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.novi.fassignment.models.User;
import com.novi.fassignment.repositories.UserRepository;
import com.novi.fassignment.utils.RandomStringGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.net.URI;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
public class InitialDataLoaderImpl {

    @Autowired
    private UserService userService;

    @Autowired
    private PaintingService paintingService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private AnswerService answerService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthorityService authorityService;


    public void createInitialUser(String username, String password, String email,
                                  boolean isAdministrator, boolean isModerator) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        String createdUsername = userService.createUser(user);
        authorityService.addAuthority(username, "ROLE_USER");
        if (isAdministrator == true) {
            authorityService.addAuthority(username, "ROLE_ADMIN");
        }
        if (isModerator == true) {
            authorityService.addAuthority(username, "ROLE_MODERATOR");
        }
    }

    public PaintingInputDto createInitialProject(String username,
                                                 String title,
                                                 String artist,
                                                 String description,
                                                 MultipartFile image,
                                                 MultipartFile[] files,
                                                 MultipartFile[] audioFiles) {

        String message = "";
        try {
            LocalDateTime dateTimePosted = LocalDateTime.now(ZoneId.of("GMT+00:01"));

            PaintingInputDto inputDto = new PaintingInputDto();
            inputDto.username = username;
            inputDto.title = title;
            inputDto.artist = artist;
            inputDto.description = description;
            inputDto.dateTimePosted = dateTimePosted;
            inputDto.lastUpdate = dateTimePosted;
            inputDto.image=image.getBytes();
            inputDto.files=files;
            inputDto.audioFiles=audioFiles;

            paintingService.createPainting(inputDto);
            message = "Painting submitted!";



            return inputDto;

        } catch (Exception exception) {
            message = "Painting could not be submitted/uploaded!";
            Optional<User> optionalUser = userService.getUser(username);
            if (!optionalUser.isPresent()) {
                throw new UsernameNotFoundException(username);}
            else{throw new StorageException(message);}
        }
    }



    public QuestionInputDto addQuestionToInitialProject(
            Long projectId,
            String username,
            String title,
            String content,
            MultipartFile image,
            MultipartFile[] files,
            MultipartFile[] musicFiles) {

        String message = "";
        try {
            LocalDateTime dateTimePosted = LocalDateTime.now(ZoneId.of("GMT+00:01"));
            QuestionInputDto inputDto= new QuestionInputDto();
            inputDto.idRelatedItem=projectId;
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
            message = "Question submitted!";

            return inputDto;

        } catch (Exception exception) {
            message = "Question could not be submitted/uploaded!";
            Optional<User> optionalUser = userService.getUser(username);
            if (!optionalUser.isPresent()) {
                throw new UsernameNotFoundException(username);}
            else{throw new StorageException(message);}
        }
    }




    public AnswerInputDto addAnswerToQuestion(
            Long questionId,
            String username,
            String title,
            String content,
            MultipartFile image,
            MultipartFile[] files,
            MultipartFile[] musicFiles) {

        String message = "";
        try {
            LocalDateTime dateTimePosted = LocalDateTime.now(ZoneId.of("GMT+00:01"));
            AnswerInputDto inputDto= new AnswerInputDto();
            inputDto.idRelatedItem=questionId;
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

            answerService.createAnswer(inputDto);
            message = "Answer submitted!";

            return inputDto;

        } catch (Exception exception) {
            message = "Question could not be submitted/uploaded!";
            Optional<User> optionalUser = userService.getUser(username);
            if (!optionalUser.isPresent()) {
                throw new UsernameNotFoundException(username);}
            else{throw new StorageException(message);}
        }
    }

}
