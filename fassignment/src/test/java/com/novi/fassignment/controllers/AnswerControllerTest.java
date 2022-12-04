package com.novi.fassignment.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.novi.fassignment.models.FileStoredInDataBase;
import com.novi.fassignment.models.Answer;
import com.novi.fassignment.models.User;
import com.novi.fassignment.repositories.AnswerRepository;
import com.novi.fassignment.repositories.QuestionRepository;
import com.novi.fassignment.repositories.UserRepository;
import com.novi.fassignment.services.*;
import com.novi.fassignment.utils.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = AnswerController.class)

public class AnswerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AnswerRepository answerRepository;

    @MockBean
    private AnswerService answerService;


    @MockBean
    private QuestionRepository questionRepository;

    @MockBean
    private QuestionService questionService;

    @MockBean
    private QuestionServiceImpl questionServiceImpl;

    @MockBean
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private FileStorageInDataBaseServiceImpl storageService;

    @MockBean
    FilesStorageService storageServiceOnDisc;

    @MockBean
    CustomUserDetailsService customUserDetailsService;

    @MockBean
    PaintingService paintingService;

    @MockBean
    PaintingServiceImpl paintingServiceImpl;


    @MockBean
    JwtUtil jwtUtil;


    @MockBean
    Answer answer;

    @MockBean
    User user;

    @WithMockUser(username = "cobergfell")

    @Test
    void whenValidPostRequestWithoutFileThenReturns204() throws Exception {
        user.setUsername("cobergfell");

        when(userService.getUser("cobergfell")).thenReturn(Optional.of(user));

        mockMvc.perform(post("/api/user/answer-upload/1")
                .param("username", "cobergfell")
                .param("title", "test1")
                .param("content", "content"))
                .andDo(print())
                .andExpect(status().isNoContent());
    }


    @Test
    void whenValidPostRequestWithFilesThenReturns204() throws Exception {
        user.setUsername("cobergfell");

        when(userService.getUser("cobergfell")).thenReturn(Optional.of(user));
        //when(answerService.createAnswer(any(Answer.class))).thenReturn(answer);

        List<MultipartFile> files = new ArrayList<>();

        String fileName = "sample-file-mock.txt";
        MockMultipartFile sampleFile1 = new MockMultipartFile(
                "first-uploaded-test-file",
                fileName,
                "text/plain",
                "This is the first file content".getBytes());
        files.add(sampleFile1);


        MockMultipartFile sampleFile2 = new MockMultipartFile(
                "second-uploaded-test-file",
                fileName,
                "text/plain",
                "This is the second file content".getBytes());

        files.add(sampleFile2);


        mockMvc.perform(multipart("/api/user/answer-upload/1")
                .file(sampleFile1)
                .file(sampleFile2)
                .param("username", "cobergfell")
                .param("title", "test1")
                .param("content", "content"))
                .andDo(print())
                //.andExpect(status().isOk());
                .andExpect(status().isNoContent());
    }


    @Test
    void whenValidPostRequestWithFilesThenStoreAnswer() throws Exception {
        user.setUsername("cobergfell");

        int i=1;
        Long l= Long.valueOf(i);
        answer.setAnswerId(l);
        answer.setUser(user);
        answer.setTitle("Shostakovich 7th symphony");
        answer.setContent("Did Shostakovich really composed his 7th symphony in Leningrad?");

        String fileName = "sample-file-mock.txt";
        MockMultipartFile sampleFile1 = new MockMultipartFile(
                "first-uploaded-test-file",
                fileName,
                "text/plain",
                "This is the first file content".getBytes());


        MockMultipartFile sampleFile2 = new MockMultipartFile(
                "second-uploaded-test-file",
                fileName,
                "text/plain",
                "This is the second file content".getBytes());

        Set<FileStoredInDataBase> filesToStoredInDataBase = new HashSet<>();
        FileStoredInDataBase fileStoredInDataBase1= new FileStoredInDataBase();
        fileStoredInDataBase1.setName("file1");
        fileStoredInDataBase1.setType("text/plain");
        fileStoredInDataBase1.setData("This is the first file content".getBytes());

        FileStoredInDataBase fileStoredInDataBase2= new FileStoredInDataBase();
        fileStoredInDataBase2.setName("file2");
        fileStoredInDataBase2.setType("text/plain");
        fileStoredInDataBase2.setData("This is the second file content".getBytes());

        filesToStoredInDataBase.add(fileStoredInDataBase1);
        filesToStoredInDataBase.add(fileStoredInDataBase2);

        answer.setFiles(filesToStoredInDataBase);

        when(userService.getUser("cobergfell")).thenReturn(Optional.of(user));
        when(answerService.createAnswerWithoutAttachment(any(Answer.class))).thenReturn(answer);


        mockMvc.perform(multipart("/api/user/answer-upload/1")
                .file(sampleFile1)
                .file(sampleFile2)
                .param("username", "cobergfell")
                .param("title", "test1")
                .param("description", "test1")
                .param("tags", "test1")
                .contentType("multipart/form-data"))
                .andDo(print())
                .andExpect(status().isNoContent());
    }


}

