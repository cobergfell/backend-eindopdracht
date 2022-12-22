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

public class PaintingControllerTestDeprecated {


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
    NoviMethod1FileUploadServiceImpl storageServiceOnDisc;

    @MockBean
    CustomUserDetailsService customUserDetailsService;

    @MockBean
    PaintingService paintingServiceMock;

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


//    @InjectMocks
//    private PaintingServiceImpl paintingService = new PaintingServiceImpl();

    @Mock
    private PaintingServiceImpl paintingService = new PaintingServiceImpl();

    @Test
    void whenValidPostRequestThenStorePainting() throws Exception {
        PaintingInputDto paintingInputDto = new PaintingInputDto();
        user.setUsername("cobergfell");
        MockMultipartFile image = new MockMultipartFile("image", new byte[1]);
        byte[] a =  {0xa, 0x2, (byte) 0xff};
        int i=1;
        Long l= Long.valueOf(i);
        paintingInputDto.setPaintingId(l);
        paintingInputDto.setUsername("cobergfell");
        paintingInputDto.setTitle("Shostakovich 7th symphony");
        paintingInputDto.setDescription("Did Shostakovich really composed his 7th symphony in Leningrad?");



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

        //files.add(sampleFile2);

        mockMvc.perform(multipart("http://localhost:8080/paintings")
                .file(sampleFile1)
                .file(sampleFile2)
                .file(image)
                .param("username", "cobergfell")
                .param("title", "test1")
                .param("description", "description")
                .contentType("multipart/form-data"))
                .andDo(print())
                //.andExpect(status().isNoContent())
                .andExpect(status().isCreated());

//
//        Assertions.assertEquals(1L,painting1.getPaintingId());
//        Assertions.assertEquals("test1",painting1.getTitle());
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

//        when(paintingRepository.findById(id)).thenReturn(Optional.of(painting));
//        mockMvc.perform(get("/api/tutorials/{id}", id)).andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(id))
//                .andExpect(jsonPath("$.title").value(painting.getTitle()))
//                .andExpect(jsonPath("$.description").value(painting.getDescription()))
//                .andExpect(jsonPath("$.published").value(painting.isPublished()))
//                .andDo(print());


            mockMvc.perform(get("/paintings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(paintingDtos.size()))
                .andDo(print());

    }



    @Test
    void shouldReturnPainting() throws Exception {

        long id = 1L;
        byte[] a = {0xa, 0x2, (byte) 0xff};
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

        var dto1 = new PaintingDto();

        dto1.setPaintingId(id);
        dto1.setUsername("cobergfell");
        dto1.setTitle("myTitle");
        dto1.setArtist("ArtistName");
        dto1.setDescription("");
        dto1.setImage(a);
        dto1.setDateTimePosted(dateTimePosted);
        dto1.setLastUpdate(dateTimePosted);




//        dto1 = PaintingDto.fromPaintingToDto(painting1);


        Mockito.when(paintingRepository.findById(id))
                .thenReturn(Optional.of(painting1));

        Mockito.when(paintingService.getPaintingById(anyLong()))
                .thenReturn(dto1);


        mockMvc.perform(get("/paintings/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("some text")))
                .andExpect(jsonPath("$.paintingId").value(id))
//                .andExpect(jsonPath("$.title").value(painting.getTitle()))
//                .andExpect(jsonPath("$.description").value(painting.getDescription()))
                //.andExpect(jsonPath("$.published").value(painting.isPublished()))
                .andDo(print());
    }





/*    @Test
    void whenValidPostRequestWithFilesThenStorePainting() throws Exception {
        user.setUsername("cobergfell");
        MockMultipartFile image = new MockMultipartFile("image", new byte[1]);
        byte[] a =  {0xa, 0x2, (byte) 0xff};
        int i=1;
        Long l= Long.valueOf(i);
        paintingInputDto.setPaintingId(l);
        paintingInputDto.setUsername("cobergfell");
        paintingInputDto.setTitle("Shostakovich 7th symphony");
        paintingInputDto.setDescription("Did Shostakovich really composed his 7th symphony in Leningrad?");



        LocalDateTime dateTimePosted = LocalDateTime.now(ZoneId.of("GMT+00:01"));
        Painting painting = new Painting();
        painting.setPaintingId(1L);
        painting.setUser(user);
        painting.setTitle("myTitle");
        painting.setDescription("Some text");
        painting.setImage(a);
        painting.setDateTimePosted(dateTimePosted);
        painting.setLastUpdate(dateTimePosted);

//        var dto = new PaintingDto();
//        dto=PaintingDto.fromPaintingToDto(painting);

        Mockito.when(paintingRepository.findById(1L))
                .thenReturn(Optional.of(painting));

//        painting.setPaintingId(l);
//        painting.setUser(user);
//        painting.setTitle("Shostakovich 7th symphony");
//        painting.setDescription("Did Shostakovich really composed his 7th symphony in Leningrad?");

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


        MultipartFile[] files = {sampleFile1,sampleFile2};


        paintingInputDto.setFiles(files);

        when(userService.getUser("cobergfell")).thenReturn(Optional.of(user));
        //when(paintingRepository.save(painting)).thenReturn(painting);
        when(paintingRepository.save(any(Painting.class))).thenReturn(painting);
        //when(paintingRepository.findById(1L)).thenReturn(Optional.of(painting));

        mockMvc.perform(multipart("http://localhost:8080/paintings")
                .file(sampleFile1)
                .file(sampleFile2)
                .file(image)
                .param("username", "cobergfell")
                .param("title", "test1")
                .param("description", "description")
                .contentType("multipart/form-data"))
            .andDo(print())
            .andExpect(status().isNoContent());

        Optional <Painting> paintingOptional = paintingRepository.findById(1L);
        Painting painting1 = paintingOptional.get();


        Assertions.assertEquals(1L,painting1.getPaintingId());

}*/







/*    @Test
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


        mockMvc.perform(post("/paintings")//.contentType(MediaType.MULTIPART_FORM_DATA)
        //.content(objectMapper.writeValueAsString(painting)))
//        .file(sampleFile1)
//        .file(sampleFile2)
//        .file(image)
        .param("username", "cobergfell")
        .param("title", "test1")
        .param("description", "description")
        .contentType("multipart/form-data"))
        .andExpect(status().isNoContent())
        //.andExpect(status().isCreated())
        .andDo(print());

//        mockMvc.perform(multipart("/paintings")
//                .file(sampleFile1)
//                .file(sampleFile2)
//                .file(image)
//                .param("username", "cobergfell")
//                .param("title", "test1")
//                .param("description", "description")
//                //.contentType("multipart/form-data"))
//                .contentType("application/json"))
//                .andDo(print())
//                .andExpect(status().isNoContent());
//                //.andExpect(status().isCreated());
    }*/






}