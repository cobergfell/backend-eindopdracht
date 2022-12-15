package com.novi.fassignment.services;

import com.novi.fassignment.controllers.dto.AnswerDto;
import com.novi.fassignment.controllers.dto.PaintingDto;
import com.novi.fassignment.controllers.dto.QuestionDto;
import com.novi.fassignment.exceptions.RecordNotFoundException;
import com.novi.fassignment.exceptions.UsernameNotFoundException;
import com.novi.fassignment.models.Answer;
import com.novi.fassignment.models.Authority;
import com.novi.fassignment.models.FileStoredInDataBase;
import com.novi.fassignment.models.User;
import com.novi.fassignment.repositories.UserRepository;
import com.novi.fassignment.utils.RandomStringGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class UserServiceImpl implements com.novi.fassignment.services.UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AnswerService answerService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private PaintingService paintingService;

    @Autowired//added 18-7-21
    private PasswordEncoder passwordEncoder;

//    @Autowired
//    private AuthorityRepository authorityRepository;

    @Override
    public Collection<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getUser(String username) {
        return userRepository.findById(username);
    }

    @Override
    public boolean userExists(String username) {
        return userRepository.existsById(username);
    }

    @Override
    public String createUser(User user) {
        String errorMessage="";

        if(userRepository.existsById(user.getUsername())) {
            errorMessage="This username already exists.";
            return errorMessage;
        }
        if(userRepository.existsByEmail(user.getEmail())) {
            errorMessage="This email already exists.";
            return errorMessage;
        }

        String randomString = RandomStringGenerator.generateAlphaNumeric(20);
        user.setApikey(randomString);
        user.setPassword(passwordEncoder.encode(user.getPassword()));//added 18-7-21
        user.setEnabled(true);
        User newUser = userRepository.save(user);
        return newUser.getUsername();
    }

    @Override
    public void deleteUser(String username) {
        Optional<User> optionalUser=userRepository.findById(username);
        if (optionalUser.isPresent()) {
                User user =optionalUser.get();
            List<AnswerDto> allAnswerDtos=answerService.getAllAnswers();
            for (int i = 0; i < allAnswerDtos.size(); i++) {
                AnswerDto answerDto = allAnswerDtos.get(i);
                if (answerDto.getUsername()==username){
                    Long id = answerDto.getAnswerId();
                    answerService.deleteAnswerById(id);
                }
            };
            List<QuestionDto> allQuestionDtos=questionService.getAllQuestions();
            for (int i = 0; i < allQuestionDtos.size(); i++) {
                QuestionDto questionDto = allQuestionDtos.get(i);
                if (questionDto.getUsername()==username){
                    Long id = questionDto.getQuestionId();
                    questionService.deleteQuestionById(id);
                }
            };
            List<PaintingDto> allPaintingsDtos=paintingService.getAllPaintings();
            for (int i = 0; i < allPaintingsDtos.size(); i++) {
                PaintingDto paintingDto = allPaintingsDtos.get(i);
                if (paintingDto.getUsername()==username){
                    Long id = paintingDto.getPaintingId();
                    paintingService.deletePaintingById(id);
                }
            };
        };
        userRepository.deleteById(username);
    }

    @Override
    public void deleteAllUsers() {
        userRepository.deleteAll();
    }

    @Override
    public void updateUser(String username, User newUser) {
        if (!userRepository.existsById(username)) throw new RecordNotFoundException();
        User user = userRepository.findById(username).get();
        user.setPassword(newUser.getPassword());
        user.setEmail(newUser.getEmail());
        user.setEnabled(newUser.isEnabled());
        user.setApikey(newUser.getApikey());
        user.setDateTimeRegisteredGMT(newUser.getDateTimeRegisteredGMT());
        user.setLastUpdate(newUser.getLastUpdate());
        user.setAuthorities(newUser.getAuthorities());
        User check=user;
        userRepository.save(user);
    }


    @Override
    public Set<Authority> getAuthorities(String username) {
        if (!userRepository.existsById(username)) throw new UsernameNotFoundException(username);
        User user = userRepository.findById(username).get();
        return user.getAuthorities();
    }

/*    @Override
    public void addAuthority(String username, String authority) {
        if (!userRepository.existsById(username)) throw new UsernameNotFoundException(username);
        User user = userRepository.findById(username).get();
        user.addAuthority(new Authority(username, authority));
        userRepository.save(user);
    }

    @Override
    public void removeAuthority(String username, String authority) {
        if (!userRepository.existsById(username)) throw new UsernameNotFoundException(username);
        User user = userRepository.findById(username).get();
        Authority authorityToRemove = user.getAuthorities().stream().filter((a) -> a.getAuthority().equalsIgnoreCase(authority)).findAny().get();
        user.removeAuthority(authorityToRemove);
        userRepository.save(user);
    }*/

}