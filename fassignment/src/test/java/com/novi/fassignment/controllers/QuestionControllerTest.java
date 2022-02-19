package com.novi.fassignment.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.novi.fassignment.models.FileStoredInDataBase;
import com.novi.fassignment.models.Question;
import com.novi.fassignment.models.User;
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
@WebMvcTest(controllers = QuestionController.class)

public class QuestionControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @Autowired
    private ObjectMapper objectMapper;


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
    private FileStorageInDataBaseService storageService;

    @MockBean
    FilesStorageService storageServiceOnDisc;


    @MockBean
    CustomUserDetailsService customUserDetailsService;

    @MockBean
    JwtUtil jwtUtil;


    @MockBean
    Question question;

    @MockBean
    User user;

    //@WithMockUser(username = "cobergfell", roles = "ADMIN")


    //@Autowired
    //private WebApplicationContext webApplicationContext;

    @WithMockUser(username = "cobergfell")

    @Test
    void whenValidPostRequestWithoutFileThenReturns204() throws Exception {
        user.setUsername("cobergfell");

/*        int i=1;
        Long l= Long.valueOf(i);
        question.setQuestionId(l);
        question.setUser(user);
        question.setTitle("Shostakovich 7th symphony");
        question.setBody("Did Shostakovich really composed his 7th symphony in Leningrad?");
        question.setTags("Shostakovich;7th symphony");*/
        when(userService.getUser("cobergfell")).thenReturn(Optional.of(user));
        //when(questionService.createQuestion(any(Question.class))).thenReturn(question);

        mockMvc.perform(post("/questions-upload-without-files")
                .param("username", "cobergfell")
                .param("title", "test1")
                .param("description", "test1")
                .param("tags", "test1"))
                //.content(objectMapper.writeValueAsString(question))
                //.contentType("application/json"))
                .andDo(print())
                //.contentType("multipart/form-data"))
                //.andExpect(status().isOk());
                .andExpect(status().isNoContent());
    }



    @Test
    void whenValidPostRequestWithFilesThenReturns204() throws Exception {
        user.setUsername("cobergfell");

/*        int i=1;
        Long l= Long.valueOf(i);
        question.setQuestionId(l);
        question.setUser(user);
        question.setTitle("Shostakovich 7th symphony");
        question.setBody("Did Shostakovich really composed his 7th symphony in Leningrad?");
        question.setTags("Shostakovich;7th symphony");*/
        when(userService.getUser("cobergfell")).thenReturn(Optional.of(user));
        //when(questionService.createQuestion(any(Question.class))).thenReturn(question);

        List<MultipartFile> files = new ArrayList<>();

        String fileName = "sample-file-mock.txt";
        MockMultipartFile sampleFile1 = new MockMultipartFile(
                "first-uploaded-test-file",
                fileName,
                "text/plain",
                "This is the first file content".getBytes());
        files.add(sampleFile1);

/*        try {
            storageService.storeQuestionFile(sampleFile1,question);
        } catch (Exception e) {};*/

        MockMultipartFile sampleFile2 = new MockMultipartFile(
                "second-uploaded-test-file",
                fileName,
                "text/plain",
                "This is the second file content".getBytes());

        files.add(sampleFile2);
/*        try {
            storageService.storeQuestionFile(sampleFile2,question);
        } catch (Exception e) {};*/
        //to do: make an array of multipart files and loop on it

        mockMvc.perform(multipart("/questions-upload-with-files-in-database")
                .file(sampleFile1)
                .file(sampleFile2)
                .param("username", "cobergfell")
                .param("title", "test1")
                .param("description", "description")
                .param("tags", "test1"))
                //.content(objectMapper.writeValueAsString(question))
                //.contentType("application/json"))

                //.contentType("multipart/form-data")
                .andDo(print())
                //.andExpect(status().isOk());
                .andExpect(status().isNoContent());
    }


    @Test
    void whenValidPostRequestWithFilesThenStoreQuestion() throws Exception {
        user.setUsername("cobergfell");

        int i=1;
        Long l= Long.valueOf(i);
        question.setQuestionId(l);
        question.setUser(user);
        question.setTitle("Shostakovich 7th symphony");
        question.setContent("Did Shostakovich really composed his 7th symphony in Leningrad?");
        question.setTags("Shostakovich;7th symphony");

        String fileName = "sample-file-mock.txt";
        MockMultipartFile sampleFile1 = new MockMultipartFile(
                "first-uploaded-test-file",
                fileName,
                "text/plain",
                "This is the first file content".getBytes());

/*        try {
            storageService.storeQuestionFile(sampleFile1,question);
        } catch (Exception e) {};*/

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


/*        try {
            storageService.storeQuestionFile(sampleFile2,question);
        } catch (Exception e) {};*/
        //to do: make an array of multipart files and loop on it
        question.setFiles(filesToStoredInDataBase);

        when(userService.getUser("cobergfell")).thenReturn(Optional.of(user));
        when(questionService.createQuestionWithoutAttachment(any(Question.class))).thenReturn(question);


        mockMvc.perform(multipart("/questions-upload-with-files-in-database")
                .file(sampleFile1)
                .file(sampleFile2)
                .param("username", "cobergfell")
                .param("title", "test1")
                .param("description", "test1")
                .param("tags", "test1")
                .contentType("multipart/form-data"))
                //.content(objectMapper.writeValueAsString(question))
                //.contentType("application/json"))

                //.contentType("multipart/form-data")
                .andDo(print())
                //.andExpect(status().isOk());
                .andExpect(status().isNoContent());
    }


}
