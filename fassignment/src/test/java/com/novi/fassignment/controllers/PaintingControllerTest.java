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

    //@WithMockUser(username = "cobergfell", roles = "ADMIN")


    //@Autowired
    //private WebApplicationContext webApplicationContext;

    @WithMockUser(username = "cobergfell")


    @Test
    void whenValidPostRequestThenReturns204() throws Exception {
        user.setUsername("cobergfell");
        MockMultipartFile image = new MockMultipartFile("image", new byte[1]);
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

        mockMvc.perform(multipart("/api/user/paintings-upload")
                .file(sampleFile1)
                .file(sampleFile2)
                .file(image)
                .param("username", "cobergfell")
                .param("title", "test1")
                .param("description", "description")
                //.param("tags", "test1"))
                //.content(objectMapper.writeValueAsString(question))
                //.contentType("application/json"))

                .contentType("multipart/form-data"))
                .andDo(print())
                //.andExpect(status().isOk());
                .andExpect(status().isNoContent());
    }


/*    @Test
    void whenValidPostRequestWithFilesThenStorePainting() throws Exception {
        user.setUsername("cobergfell");

        int i=1;
        Long l= Long.valueOf(i);
        painting.setPaintingId(l);
        painting.setUser(user);
        painting.setTitle("Test title");
        painting.setDescription("This is the description");
        //MockMultipartFile image = new MockMultipartFile("image", new byte[1]);
        painting.setImage(new byte[1]);

        int i=1;
        Long l= Long.valueOf(i);
        paintingInputDto.setPaintingId(l);
        paintingInputDto.setUsername("cobergfell");
        paintingInputDto.setTitle("Test title");
        paintingInputDto.setArtist("Test artist name");
        paintingInputDto.setDescription("This is the description");
        //MockMultipartFile image = new MockMultipartFile("image", new byte[1]);
        paintingInputDto.setImage(new byte[1]);


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


        MockMultipartFile sampleFile3 = new MockMultipartFile(
                "third-uploaded-test-file",
                fileName,
                "text/plain",
                "This is the third file content".getBytes());

        MockMultipartFile[] sampleFiles = {sampleFile1, sampleFile2};
        MockMultipartFile[] sampleMusicFiles = {sampleFile3};
        //Set<FileStoredInDataBase> filesToStoredInDataBase = new HashSet<>();
        //Set<MusicFileStoredInDataBase> audioFilesToStoredInDataBase = new HashSet<>();

        List<MultipartFile> list = Arrays.asList(new MultipartFile[]{sampleFile1, sampleFile2});

//        FileStoredInDataBase fileStoredInDataBase1= new FileStoredInDataBase();
//        fileStoredInDataBase1.setName("file1");
//        fileStoredInDataBase1.setType("text/plain");
//        fileStoredInDataBase1.setData("This is the first file content".getBytes());
//
//        FileStoredInDataBase fileStoredInDataBase2= new FileStoredInDataBase();
//        fileStoredInDataBase2.setName("file2");
//        fileStoredInDataBase2.setType("text/plain");
//        fileStoredInDataBase2.setData("This is the second file content".getBytes());
//
//        filesToStoredInDataBase.add(fileStoredInDataBase1);
//        filesToStoredInDataBase.add(fileStoredInDataBase2);
//
//
//        MusicFileStoredInDataBase fileStoredInDataBase3= new MusicFileStoredInDataBase();
//        fileStoredInDataBase3.setName("file3");
//        fileStoredInDataBase3.setType("text/plain");
//        fileStoredInDataBase3.setData("This is the third file content".getBytes());
//
//        audioFilesToStoredInDataBase.add(fileStoredInDataBase3);

        //painting.setFiles(filesToStoredInDataBase);
        //painting.setMusicFiles(audioFilesToStoredInDataBase);

        paintingInputDto.setFiles(sampleFiles);
        paintingInputDto.setMusicFiles(sampleMusicFiles);

        //when(userService.getUser("cobergfell")).thenReturn(Optional.of(user));

       //when(paintingService.createPainting(any(PaintingInputDto.class))).thenReturn();
        //Mockito.doNothing().when(paintingService.createPainting(any(PaintingInputDto.class)););

        mockMvc.perform(multipart("http://localhost:8080/api/user/paintings-upload")
                .file(sampleFile1)
                //.file(sampleFile2)
                //.file(sampleFile3)
                .param("username", "cobergfell")
                .param("title", "test1")
                .param("description", "test1")
                .param("artist", "test1")
                //.contentType("multipart/form-data"))
                //.content(objectMapper.writeValueAsString(question))
                //.contentType("application/json"))

                .contentType("multipart/form-data"))
                .andDo(print())
                //.andExpect(status().isOk());
                .andExpect(status().isNoContent());
    }*/


}

