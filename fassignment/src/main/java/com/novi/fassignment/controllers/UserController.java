package com.novi.fassignment.controllers;

import com.novi.fassignment.exceptions.BadRequestException;
import com.novi.fassignment.exceptions.RecordNotFoundException;
import com.novi.fassignment.models.*;
import com.novi.fassignment.repositories.AuthorityRepository;
import com.novi.fassignment.repositories.UserRepository;
import com.novi.fassignment.services.AuthorityService;
import com.novi.fassignment.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.List;

@RestController
@CrossOrigin(origins ="*")
@RequestMapping(value = "users")

public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private AuthorityService authorityService;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private UserRepository userRepository;


    @GetMapping(value = "")
    public ResponseEntity<Object> getUsers() {
        return ResponseEntity.ok().body(userService.getUsers());
    }

    @GetMapping(value = "/{username}")
    public ResponseEntity<Object> getUser(@PathVariable("username") String username) {
        return ResponseEntity.ok().body(userService.getUser(username));
    }


    @PostMapping(value = "")
    public ResponseEntity<Object> createUser(@RequestBody User user) {
        String newUsername = userService.createUser(user);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{username}")
                .buildAndExpand(newUsername).toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping(value = "/{username}")
    public ResponseEntity<Object> updateUser(@PathVariable("username") String username,
                                             @RequestParam("newUsername") String newUsername,
                                             @RequestParam("email") String email,
                                             @RequestParam("enabled")  boolean enabled,
                                             @RequestParam("moderator")  boolean moderator,
                                             @RequestParam("administrator")  boolean administrator){

        LocalDateTime dateTimePosted = LocalDateTime.now(ZoneId.of("GMT+00:00"));
        ZonedDateTime zonedDateTimePosted = dateTimePosted.atZone(ZoneId.of("GMT+00:00"));
        Optional<User> optionalUser=userService.getUser(username);
        if (optionalUser.isPresent()) {
            if (username.equals(newUsername)){
                User user =optionalUser.get();
                user.setEmail(email);
                user.setEnabled(enabled);

                userService.updateUser(username, user);
            }
            else {
                User newUser = new User();
                newUser.setUsername(newUsername);
                newUser.setEmail(email);
                userService.updateUser(username, newUser);
            }

        } else {
            throw new RecordNotFoundException("id does not exist");
        }


        authorityService.removeAllAuthorities(username);
        System.out.println("Arrays.asList(ERole.values()): " + Arrays.asList(ERole.values()));
        List<ERole> test= Arrays.asList(ERole.values());


        Authority user_authority = new Authority(username, "ROLE_USER");
        authorityRepository.save(user_authority);

        if(moderator==true){
            Authority moderator_authority = new Authority(username, "ROLE_MODERATOR");
            authorityRepository.save(moderator_authority);
        }

        if(administrator==true){
            Authority admin_authority = new Authority(username, "ROLE_ADMIN");
            authorityRepository.save(admin_authority);
        }

        return ResponseEntity.noContent().build();
    }




    @DeleteMapping(value = "/{username}")
    public ResponseEntity<Object> deleteUser(@PathVariable("username") String username) {
        userService.deleteUser(username);
        return ResponseEntity.noContent().build();
    }


    @DeleteMapping(value ="")
    public ResponseEntity<HttpStatus> deleteAllUsers() {
        try {
            userService.deleteAllUsers();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


/*
    @GetMapping(value = "authorities/{username}")
    public ResponseEntity<Object> getUserAuthorities(@PathVariable("username") String username) {
        return ResponseEntity.ok().body(userService.getAuthorities(username));
    }


    @PostMapping(value = "authorities/add/{username}/{authority}")
    public ResponseEntity<Object> addAuthority(@PathVariable("username") String username,@PathVariable("authority") String authority) {
        try {
            Optional<User> optionalUser=userService.getUser(username);
            if (optionalUser.isPresent()) {
                authorityService.addAuthority(username, authority);;
            }
            return ResponseEntity.noContent().build();
        }
        catch (Exception ex) {
            throw new BadRequestException();
        }
    }

    @PostMapping(value = "authorities/delete/{username}/{authority}")
    public ResponseEntity<Object> deleteAuthority(@PathVariable("username") String username,@PathVariable("authority") String authority) {
        try {
            Optional<User> optionalUser=userService.getUser(username);
            if (optionalUser.isPresent()) {
                authorityService.removeAuthority(username, authority);;
            }
            return ResponseEntity.noContent().build();
        }
        catch (Exception ex) {
            throw new BadRequestException();
        }
    }
    @DeleteMapping(value = "/authorities/{username}/{authority}")
    public ResponseEntity<Object> deleteUserAuthority(@PathVariable("username") String username, @PathVariable("authority") String authority) {
        authorityService.removeAuthority(username, authority);
        return ResponseEntity.noContent().build();
    }*/

}