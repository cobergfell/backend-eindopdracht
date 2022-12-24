package com.novi.fassignment.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.novi.fassignment.controllers.dto.AnswerDto;
import com.novi.fassignment.controllers.dto.AnswerInputDto;
import com.novi.fassignment.controllers.dto.UserDto;
import com.novi.fassignment.models.*;
import com.novi.fassignment.repositories.AnswerRepository;
import com.novi.fassignment.repositories.QuestionRepository;
import com.novi.fassignment.repositories.UserRepository;
import com.novi.fassignment.services.*;
import com.novi.fassignment.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.hamcrest.Matchers.*;


@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = AnswerController.class)

public class AnswerControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    AnswerService answerService;

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
    NoviMethod1FileUploadServiceImpl storageServiceOnDisc;

    @MockBean
    CustomUserDetailsService customUserDetailsService;


    @MockBean
    AnswerServiceImpl answerServiceImpl;

    @MockBean
    AnswerRepository answerRepository;

    @MockBean
    JwtUtil jwtUtil;

    @MockBean
    Question question;

    @MockBean
    Answer answer;

    @MockBean
    AnswerDto answerDto;

    @MockBean
    AnswerInputDto answerInputDto;

    @MockBean
    User user;


    //@WithMockUser(username = "cobergfell")

    @BeforeEach
    void setup() {
        user.setUsername("cobergfell");
        user.setPassword("mySecretPassword");
        user.setEmail("fake@mail.com");
        UserDto userDto=UserDto.fromUser(user);
    }


//    @Mock
//    private AnswerServiceImpl answerService = new AnswerServiceImpl();

    @Test
    void whenValidPostRequestThenStoreAnswer() throws Exception {
        AnswerInputDto answerInputDto = new AnswerInputDto();
        user.setUsername("cobergfell");
        MockMultipartFile image = new MockMultipartFile("image", new byte[1]);
        byte[] a =  {0xa, 0x2, (byte) 0xff};
//        int i=1;
//        Long id= Long.valueOf(i);
        long id = 1L;
        LocalDateTime dateTimePosted = LocalDateTime.now(ZoneId.of("GMT+00:01"));
        Answer answer = new Answer();
        answer.setAnswerId(1L);
        answer.setUser(user);
        answer.setTitle("myTitle");
        answer.setContent("Some text");
        answer.setImage(a);
        answer.setDateTimePosted(dateTimePosted);
        answer.setLastUpdate(dateTimePosted);

        when(userService.getUser("cobergfell")).thenReturn(Optional.of(user));

        Mockito.when(answerRepository.findById(1L))
                .thenReturn(Optional.of(answer));

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

        mockMvc.perform(multipart("/answers/{id}", id)
                .file(sampleFile1)
                .file(sampleFile2)
                .file(image)
                .param("username", "cobergfell")
                .param("title", "This is a test title")
                .param("content", "This is a test description")
                .contentType("multipart/form-data"))
                .andExpect(jsonPath("$.title").value("This is a test title"))
                .andExpect(jsonPath("$.content").value("This is a test description"))
                .andDo(print())
                .andExpect(status().isCreated());
    }


    @Test
    void shouldReturnListOfAnswers() throws Exception {

        byte[] a =  {0xa, 0x2, (byte) 0xff};
        LocalDateTime dateTimePosted = LocalDateTime.now(ZoneId.of("GMT+00:01"));
        Answer answer1 = new Answer();
        answer1.setAnswerId(1L);
        answer1.setUser(user);
        answer1.setTitle("myTitle");
        answer1.setContent("");
        answer1.setImage(a);
        answer1.setDateTimePosted(dateTimePosted);
        answer1.setLastUpdate(dateTimePosted);

        Answer answer2 = new Answer();
        answer2.setAnswerId(2L);
        answer2.setUser(user);
        answer2.setTitle("myTitle");
        answer2.setContent("");
        answer2.setImage(a);
        answer2.setDateTimePosted(dateTimePosted);
        answer2.setLastUpdate(dateTimePosted);

        var dto1 = new AnswerDto();
        dto1=AnswerDto.fromAnswerToDto(answer1);

        var dto2 = new AnswerDto();
        dto2=AnswerDto.fromAnswerToDto(answer2);

        List<Answer> answerList = Arrays.asList(answer1, answer2);

        Mockito.when(answerRepository.findAll(Sort.by("answerId").ascending()))
                .thenReturn(answerList);

        List<AnswerDto> answerDtos = answerService.getAllAnswers();

        mockMvc.perform(get("/answers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(answerDtos.size()))
                .andDo(print());

    }



    @Test
    void shouldReturnAnswer() throws Exception {

        User user = new User();
        user.setUsername("cobergfell");
        user.setPassword("mySecretPassword");
        user.setEmail("fake@mail.com");

        long id = 1L;
        byte[] a = {0xa, 0x2, (byte) 0xff};
        LocalDateTime dateTimePosted = LocalDateTime.now(ZoneId.of("GMT+00:01"));

        var dto = new AnswerDto();

        dto.setAnswerId(id);
        dto.setUsername("cobergfell");
        dto.setTitle("myTitle");
        dto.setContent("some text");
        dto.setImage(a);
        dto.setDateTimePosted(dateTimePosted);
        dto.setLastUpdate(dateTimePosted);

        Mockito.when(answerService.getAnswerById(anyLong()))
                .thenReturn(dto);

        mockMvc.perform(get("/answers/{id}", id))
                .andExpect(content().string(containsString("some text")))
                .andExpect(jsonPath("$.title").value(dto.getTitle()))
                .andExpect(jsonPath("$.content").value(dto.getContent()))
                .andExpect(status().isOk())
                .andDo(print());
    }


}