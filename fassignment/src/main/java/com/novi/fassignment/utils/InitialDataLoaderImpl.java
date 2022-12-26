package com.novi.fassignment.utils;


import com.novi.fassignment.controllers.dto.FileStoredInDataBaseInputDto;
import com.novi.fassignment.controllers.dto.PaintingInputDto;
import com.novi.fassignment.exceptions.StorageException;
import com.novi.fassignment.exceptions.UsernameNotFoundException;
import com.novi.fassignment.models.*;
import com.novi.fassignment.repositories.UserRepository;
import com.novi.fassignment.services.PaintingService;
import com.novi.fassignment.services.UserService;
import com.novi.fassignment.services.AuthorityService;
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

}
