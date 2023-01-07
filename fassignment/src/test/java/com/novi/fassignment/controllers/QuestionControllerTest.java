package com.novi.fassignment.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.novi.fassignment.controllers.dto.QuestionDto;
import com.novi.fassignment.controllers.dto.QuestionInputDto;
import com.novi.fassignment.controllers.dto.UserDto;
import com.novi.fassignment.models.*;
import com.novi.fassignment.repositories.*;
import com.novi.fassignment.repositories.QuestionRepository;
import com.novi.fassignment.services.*;
import com.novi.fassignment.utils.InitialDataLoaderImpl;
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
@WebMvcTest(controllers = QuestionController.class)

public class QuestionControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    QuestionService questionService;

    @MockBean
    private QuestionRepository questionRepository;


    @MockBean
    private QuestionServiceImpl questionServiceImpl;

    @MockBean
    PaintingServiceImpl paintingServiceImpl;

    @MockBean
    PaintingRepository paintingRepository;

    @MockBean
    AnswerServiceImpl answerServiceImpl;

    @MockBean
    AnswerRepository answerRepository;

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
    JwtUtil jwtUtil;

    @MockBean
    Question question;


    @MockBean
    QuestionDto questionDto;

    @MockBean
    QuestionInputDto questionInputDto;

    @MockBean
    User user;

    @MockBean
    InitialDataLoaderImpl initialDataLoaderImpl;

    @BeforeEach
    void setup() {
        user.setUsername("cobergfell");
        user.setPassword("mySecretPassword");
        user.setEmail("fake@mail.com");
        UserDto userDto=UserDto.fromUser(user);
    }


//    @Mock
//    private QuestionServiceImpl questionService = new QuestionServiceImpl();

    @Test
    void whenValidPostRequestThenStoreQuestion() throws Exception {
       QuestionInputDto questionInputDto = new QuestionInputDto();
        user.setUsername("cobergfell");
        MockMultipartFile image = new MockMultipartFile("image", new byte[1]);
        byte[] a =  {0xa, 0x2, (byte) 0xff};
//        int i=1;
//        Long id= Long.valueOf(i);
        long id = 1L;
        LocalDateTime dateTimePosted = LocalDateTime.now(ZoneId.of("GMT+00:01"));
        Question question = new Question();
        question.setQuestionId(1L);
        question.setUser(user);
        question.setTitle("myTitle");
        question.setContent("Some text");
        question.setImage(a);
        question.setDateTimePosted(dateTimePosted);
        question.setLastUpdate(dateTimePosted);

        when(userService.getUser("cobergfell")).thenReturn(Optional.of(user));

        Mockito.when(questionRepository.findById(1L))
                .thenReturn(Optional.of(question));

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

        mockMvc.perform(multipart("/questions/{id}", id)
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
    void shouldReturnListOfQuestions() throws Exception {

        byte[] a =  {0xa, 0x2, (byte) 0xff};
        LocalDateTime dateTimePosted = LocalDateTime.now(ZoneId.of("GMT+00:01"));
        Question question1 = new Question();
        question1.setQuestionId(1L);
        question1.setUser(user);
        question1.setTitle("myTitle");
        question1.setContent("");
        question1.setImage(a);
        question1.setDateTimePosted(dateTimePosted);
        question1.setLastUpdate(dateTimePosted);

        Question question2 = new Question();
        question2.setQuestionId(2L);
        question2.setUser(user);
        question2.setTitle("myTitle");
        question2.setContent("");
        question2.setImage(a);
        question2.setDateTimePosted(dateTimePosted);
        question2.setLastUpdate(dateTimePosted);

        var dto1 = new QuestionDto();
        dto1=QuestionDto.fromQuestionToDto(question1);

        var dto2 = new QuestionDto();
        dto2=QuestionDto.fromQuestionToDto(question2);

        List<Question> questionList = Arrays.asList(question1, question2);

        Mockito.when(questionRepository.findAll(Sort.by("questionId").ascending()))
                .thenReturn(questionList);

        List<QuestionDto> questionDtos = questionService.getAllQuestions();

        mockMvc.perform(get("/questions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(questionDtos.size()))
                .andDo(print());

    }



    @Test
    void shouldReturnQuestion() throws Exception {

        User user = new User();
        user.setUsername("cobergfell");
        user.setPassword("mySecretPassword");
        user.setEmail("fake@mail.com");

        long id = 1L;
        byte[] a = {0xa, 0x2, (byte) 0xff};
        LocalDateTime dateTimePosted = LocalDateTime.now(ZoneId.of("GMT+00:01"));

        var dto = new QuestionDto();

        dto.setQuestionId(id);
        dto.setUsername("cobergfell");
        dto.setTitle("myTitle");
        dto.setContent("some text");
        dto.setImage(a);
        dto.setDateTimePosted(dateTimePosted);
        dto.setLastUpdate(dateTimePosted);

        Mockito.when(questionService.getQuestionById(anyLong()))
                .thenReturn(dto);

        mockMvc.perform(get("/questions/{id}", id))
                .andExpect(content().string(containsString("some text")))
                .andExpect(jsonPath("$.title").value(dto.getTitle()))
                .andExpect(jsonPath("$.content").value(dto.getContent()))
                .andExpect(status().isOk())
                .andDo(print());
    }


}