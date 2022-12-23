package com.novi.fassignment.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.novi.fassignment.controllers.dto.AnswerDto;
import com.novi.fassignment.controllers.dto.PaintingDto;
import com.novi.fassignment.controllers.dto.PaintingInputDto;
import com.novi.fassignment.controllers.dto.UserDto;
import com.novi.fassignment.models.*;
import com.novi.fassignment.repositories.PaintingRepository;
import com.novi.fassignment.repositories.QuestionRepository;
import com.novi.fassignment.repositories.UserRepository;
import com.novi.fassignment.services.*;
import com.novi.fassignment.utils.JwtUtil;
import org.apache.catalina.webresources.FileResource;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;




import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.fasterxml.jackson.databind.ObjectMapper;




import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

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

    @Autowired
    PaintingService paintingService;

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

//    @MockBean
//    PaintingService paintingServiceMock;

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


    //@WithMockUser(username = "cobergfell")

    @BeforeEach
    void setup() {
        user.setUsername("cobergfell");
        user.setPassword("mySecretPassword");
        user.setEmail("fake@mail.com");
        UserDto userDto=UserDto.fromUser(user);
    }


//    @Mock
//    private PaintingServiceImpl paintingService = new PaintingServiceImpl();

    @Test
    void whenValidPostRequestThenStorePainting() throws Exception {
        PaintingInputDto paintingInputDto = new PaintingInputDto();
        user.setUsername("cobergfell");
        MockMultipartFile image = new MockMultipartFile("image", new byte[1]);
        byte[] a =  {0xa, 0x2, (byte) 0xff};
        int i=1;
        Long l= Long.valueOf(i);

        LocalDateTime dateTimePosted = LocalDateTime.now(ZoneId.of("GMT+00:01"));
        Painting painting = new Painting();
        painting.setPaintingId(1L);
        painting.setUser(user);
        painting.setTitle("myTitle");
        painting.setDescription("Some text");
        painting.setImage(a);
        painting.setDateTimePosted(dateTimePosted);
        painting.setLastUpdate(dateTimePosted);


        when(userService.getUser("cobergfell")).thenReturn(Optional.of(user));


        Mockito.when(paintingRepository.findById(1L))
                .thenReturn(Optional.of(painting));

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

        mockMvc.perform(multipart("http://localhost:8080/paintings")
                .file(sampleFile1)
                .file(sampleFile2)
                .file(image)
                .param("username", "cobergfell")
                .param("title", "This is a test title")
                .param("artist", "Picasso")
                .param("description", "This is a test description")
                .contentType("multipart/form-data"))
                .andExpect(jsonPath("$.title").value("This is a test title"))
                .andExpect(jsonPath("$.artist").value("Picasso"))
                .andExpect(jsonPath("$.description").value("This is a test description"))
                .andDo(print())
                .andExpect(status().isCreated());
    }


        @Test
    void shouldReturnListOfPaintings() throws Exception {

            byte[] a =  {0xa, 0x2, (byte) 0xff};
            LocalDateTime dateTimePosted = LocalDateTime.now(ZoneId.of("GMT+00:01"));
            Painting painting1 = new Painting();
            painting1.setPaintingId(1L);
            painting1.setUser(user);
            painting1.setTitle("myTitle");
            painting1.setArtist("ArtistName");
            painting1.setDescription("");
            painting1.setImage(a);
            painting1.setDateTimePosted(dateTimePosted);
            painting1.setLastUpdate(dateTimePosted);

            Painting painting2 = new Painting();
            painting2.setPaintingId(2L);
            painting2.setUser(user);
            painting2.setTitle("myTitle");
            painting2.setArtist("ArtistName");
            painting2.setDescription("");
            painting2.setImage(a);
            painting2.setDateTimePosted(dateTimePosted);
            painting2.setLastUpdate(dateTimePosted);

            var dto1 = new PaintingDto();
            dto1=PaintingDto.fromPaintingToDto(painting1);

            var dto2 = new PaintingDto();
            dto2=PaintingDto.fromPaintingToDto(painting2);

            List<Painting> paintingList = Arrays.asList(painting1, painting2);

            Mockito.when(paintingRepository.findAll(Sort.by("paintingId").ascending()))
                    .thenReturn(paintingList);

            List<PaintingDto> paintingDtos = paintingService.getAllPaintingsByAscId();

            mockMvc.perform(get("/paintings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(paintingDtos.size()))
                .andDo(print());

    }



    @Test
    void shouldReturnPainting() throws Exception {

        User user = new User();
        user.setUsername("cobergfell");
        user.setPassword("mySecretPassword");
        user.setEmail("fake@mail.com");

        long id = 1L;
        byte[] a = {0xa, 0x2, (byte) 0xff};
        LocalDateTime dateTimePosted = LocalDateTime.now(ZoneId.of("GMT+00:01"));

        var dto = new PaintingDto();

        dto.setPaintingId(id);
        dto.setUsername("cobergfell");
        dto.setTitle("myTitle");
        dto.setArtist("ArtistName");
        dto.setDescription("some text");
        dto.setImage(a);
        dto.setDateTimePosted(dateTimePosted);
        dto.setLastUpdate(dateTimePosted);

        Mockito.when(paintingService.getPaintingById(anyLong()))
                .thenReturn(dto);

        mockMvc.perform(get("/paintings/{id}", id))
                .andExpect(content().string(containsString("some text")))
                .andExpect(jsonPath("$.title").value(dto.getTitle()))
                .andExpect(jsonPath("$.artist").value(dto.getArtist()))
                .andExpect(jsonPath("$.description").value(dto.getDescription()))
                .andExpect(status().isOk())
                .andDo(print());
    }


}