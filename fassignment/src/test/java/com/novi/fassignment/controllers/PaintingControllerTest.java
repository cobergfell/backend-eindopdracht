package com.novi.fassignment.controllers;

import com.novi.fassignment.controllers.dto.AnswerDto;
import com.novi.fassignment.controllers.dto.PaintingDto;
import com.novi.fassignment.controllers.dto.PaintingInputDto;
import com.novi.fassignment.models.*;
import com.novi.fassignment.repositories.PaintingRepository;
import com.novi.fassignment.repositories.QuestionRepository;
import com.novi.fassignment.repositories.UserRepository;
import com.novi.fassignment.services.*;
import com.novi.fassignment.utils.JwtUtil;
import static org.mockito.ArgumentMatchers.any;
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

@WebMvcTest(PaintingController.class)
public class PaintingControllerTest {
    @MockBean
    private PaintingRepository PaintingRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

//    @Test
//    void shouldCreatePainting() throws Exception {
//        Painting painting = new Painting(1, "Spring Boot @WebMvcTest", "Description", true);
//
//        mockMvc.perform(post("/paintings").contentType(MediaType.MULTIPART_FORM_DATA)
//                .content(objectMapper.writeValueAsString(painting)))
//                .andExpect(status().isCreated())
//                .andDo(print());
//    }
//
//
//
//    @Test
//    void shouldReturnPainting() throws Exception {
//        long id = 1L;
//        Painting painting = new Painting(id, "Spring Boot @WebMvcTest", "Description", true);
//
//        when(paintingRepository.findById(id)).thenReturn(Optional.of(painting));
//        mockMvc.perform(get("/api/tutorials/{id}", id)).andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(id))
//                .andExpect(jsonPath("$.title").value(painting.getTitle()))
//                .andExpect(jsonPath("$.description").value(painting.getDescription()))
//                .andExpect(jsonPath("$.published").value(painting.isPublished()))
//                .andDo(print());
//    }

//    @Test
//    void shouldReturnNotFoundPainting() throws Exception {
//        long id = 1L;
//
//        when(paintingRepository.findById(id)).thenReturn(Optional.empty());
//        mockMvc.perform(get("/api/tutorials/{id}", id))
//                .andExpect(status().isNotFound())
//                .andDo(print());
//    }
//
//    @Test
//    void shouldReturnListOfPaintings() throws Exception {
//        List<Painting> paintings = new ArrayList<>(
//                Arrays.asList(new Painting(1, "Spring Boot @WebMvcTest 1", "Description 1", true),
//                        new Painting(2, "Spring Boot @WebMvcTest 2", "Description 2", true),
//                        new Painting(3, "Spring Boot @WebMvcTest 3", "Description 3", true)));
//
//        when(paintingRepository.findAll()).thenReturn(paintings);
//        mockMvc.perform(get("/api/paintings"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.size()").value(Paintings.size()))
//                .andDo(print());
//    }
//
//    @Test
//    void shouldReturnListOfPaintingsWithFilter() throws Exception {
//        List<Painting> paintings = new ArrayList<>(
//                Arrays.asList(new Painting(1, "Spring Boot @WebMvcTest", "Description 1", true),
//                        new Painting(3, "Spring Boot Web MVC", "Description 3", true)));
//
//        String title = "Boot";
//        MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();
//        paramsMap.add("title", title);
//
//        when(paintingRepository.findByTitleContaining(title)).thenReturn(paintings);
//        mockMvc.perform(get("/api/tutorials").params(paramsMap))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.size()").value(paintings.size()))
//                .andDo(print());
//
//        paintings = Collections.emptyList();
//
//        when(paintingRepository.findByTitleContaining(title)).thenReturn(paintings);
//        mockMvc.perform(get("/api/tutorials").params(paramsMap))
//                .andExpect(status().isNoContent())
//                .andDo(print());
//    }
//
//    @Test
//    void shouldReturnNoContentWhenFilter() throws Exception {
//        String title = "BezKoder";
//        MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();
//        paramsMap.add("title", title);
//
//        List<Painting> paintings = Collections.emptyList();
//
//        when(paintingRepository.findByTitleContaining(title)).thenReturn(paintings);
//        mockMvc.perform(get("/api/tutorials").params(paramsMap))
//                .andExpect(status().isNoContent())
//                .andDo(print());
//    }
//
//    @Test
//    void shouldUpdatePainting() throws Exception {
//        long id = 1L;
//
//        Painting painting = new Painting(id, "Spring Boot @WebMvcTest", "Description", false);
//        Painting updatedtutorial = new Painting(id, "Updated", "Updated", true);
//
//        when(paintingRepository.findById(id)).thenReturn(Optional.of(painting));
//        when(paintingRepository.save(any(Painting.class))).thenReturn(updatedtutorial);
//
//        mockMvc.perform(put("/api/tutorials/{id}", id).contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(updatedtutorial)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.title").value(updatedtutorial.getTitle()))
//                .andExpect(jsonPath("$.description").value(updatedtutorial.getDescription()))
//                .andExpect(jsonPath("$.published").value(updatedtutorial.isPublished()))
//                .andDo(print());
//    }
//
//    @Test
//    void shouldReturnNotFoundUpdatePainting() throws Exception {
//        long id = 1L;
//
//        Painting updatedtutorial = new Painting(id, "Updated", "Updated", true);
//
//        when(paintingRepository.findById(id)).thenReturn(Optional.empty());
//        when(paintingRepository.save(any(Painting.class))).thenReturn(updatedtutorial);
//
//        mockMvc.perform(put("/api/tutorials/{id}", id).contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(updatedtutorial)))
//                .andExpect(status().isNotFound())
//                .andDo(print());
//    }
//
//    @Test
//    void shouldDeletePainting() throws Exception {
//        long id = 1L;
//
//        doNothing().when(paintingRepository).deleteById(id);
//        mockMvc.perform(delete("/api/tutorials/{id}", id))
//                .andExpect(status().isNoContent())
//                .andDo(print());
//    }
//
//    @Test
//    void shouldDeleteAllPaintings() throws Exception {
//        doNothing().when(paintingRepository).deleteAll();
//        mockMvc.perform(delete("/api/tutorials"))
//                .andExpect(status().isNoContent())
//                .andDo(print());
//    }
}