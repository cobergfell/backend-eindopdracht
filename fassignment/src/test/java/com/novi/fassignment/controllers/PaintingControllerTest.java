package com.novi.fassignment.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.novi.fassignment.controllers.dto.PaintingDto;
import com.novi.fassignment.controllers.dto.PaintingInputDto;
import com.novi.fassignment.models.*;
import com.novi.fassignment.repositories.PaintingRepository;
import com.novi.fassignment.repositories.QuestionRepository;
import com.novi.fassignment.repositories.UserRepository;
import com.novi.fassignment.services.*;
import com.novi.fassignment.utils.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
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
@WebMvcTest(controllers = PaintingController.class)

public class PaintingControllerTest {

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
    PaintingRepository paintingRepository;

    @MockBean
    JwtUtil jwtUtil;

    @MockBean
    Question question;

    @MockBean
    Painting painting;

    @MockBean
    PaintingDto paintingDto;

    @MockBean
    PaintingInputDto paintingInputDto;

    @MockBean
    User user;


    @WithMockUser(username = "cobergfell")


    @Test
    void whenValidPostRequestThenReturns204() throws Exception {
        user.setUsername("cobergfell");
        MockMultipartFile image = new MockMultipartFile("image", new byte[1]);
        when(userService.getUser("cobergfell")).thenReturn(Optional.of(user));

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

        mockMvc.perform(multipart("/api/user/paintings-upload")
                .file(sampleFile1)
                .file(sampleFile2)
                .file(image)
                .param("username", "cobergfell")
                .param("title", "test1")
                .param("description", "description")
                .contentType("multipart/form-data"))
                .andDo(print())
                .andExpect(status().isNoContent());
    }
}