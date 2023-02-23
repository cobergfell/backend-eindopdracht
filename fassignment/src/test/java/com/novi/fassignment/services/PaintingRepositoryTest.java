package com.novi.fassignment.services;

import com.novi.fassignment.controllers.dto.UserDto;
import com.novi.fassignment.models.Painting;
import com.novi.fassignment.models.User;
import com.novi.fassignment.repositories.PaintingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class PaintingRepositoryTest {

    // @InjectMocks vs @Mocks: see  https://howtodoinjava.com/mockito/mockito-mock-injectmocks/


    @InjectMocks PaintingServiceImpl paintingService;

    @Mock
    private PaintingRepository paintingRepositoryMock;

    private Painting painting = new Painting();

    @InjectMocks
    UserServiceImpl userService;

    private User user = new User();

    @BeforeEach
    void setup() {
        user.setUsername("cobergfell");
        user.setPassword("bla");
        user.setEmail("fake@mail.com");
        UserDto userDto=UserDto.fromUser(user);
    }



    @Test
    void whenSavedFromRepositoryThenFindsById() {
        long id = 1L;
        Painting newPainting = new Painting();
        newPainting.setPaintingId(id);
        newPainting.setTitle("myTestTitle");
        paintingRepositoryMock.save(newPainting);

        // when
        Optional<Painting> paintingOpt= paintingRepositoryMock.findById(id);
        if (paintingOpt.isPresent()) {
            Painting painting = paintingOpt.get();

        // then
        String expected = "myTestTitle";
        String actual = painting.getTitle();
        assertEquals(expected, actual);}

    }



}