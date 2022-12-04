package com.novi.fassignment.services;

import com.novi.fassignment.controllers.dto.PaintingDto;
import com.novi.fassignment.controllers.dto.UserDto;
import com.novi.fassignment.models.Painting;
import com.novi.fassignment.models.User;
import com.novi.fassignment.repositories.PaintingRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class PaintingServiceImplTest {

    // @InjectMocks vs @Mocks: see  https://howtodoinjava.com/mockito/mockito-mock-injectmocks/

    @InjectMocks PaintingServiceImpl paintingService;



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

    @Mock
    private PaintingRepository paintingRepositoryMock;


    private Painting painting = new Painting();

    @Test
    void paintingServiceGetPaintingByIdTest() {
        byte[] a =  {0xa, 0x2, (byte) 0xff};
        LocalDateTime dateTimePosted = LocalDateTime.now(ZoneId.of("GMT+00:01"));
        Painting painting = new Painting();
        painting.setPaintingId(1L);
        painting.setUser(user);
        painting.setTitle("myTitle");
        painting.setArtist("ArtistName");
        painting.setDescription("");
        painting.setImage(a);
        painting.setDateTimePosted(dateTimePosted);
        painting.setLastUpdate(dateTimePosted);

        var dto = new PaintingDto();
        dto=PaintingDto.fromPaintingToDto(painting);

        Mockito.when(paintingRepositoryMock.findById(1L))
                .thenReturn(Optional.of(painting));

        PaintingDto actual = paintingService.getPaintingById(painting.getPaintingId());
        Assertions.assertSame(painting.getPaintingId(), actual.getPaintingId());
    }


    @Test
    void paintingServiceGetAllPaintingsByAscIdTest() {
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

        Mockito.when(paintingRepositoryMock.findAll(Sort.by("paintingId").ascending()))
                .thenReturn(paintingList);

        List<PaintingDto> actual = paintingService.getAllPaintingsByAscId();

        PaintingDto actualDto1 = actual.get(0);
        PaintingDto actualDto2 = actual.get(1);

        Assertions.assertEquals(1L,actualDto1.paintingId);
        Assertions.assertEquals(2L,actualDto2.paintingId);

    }


        @Test
    void searchingUnknownIdShouldReturnErrorMessage() {
        long id = 1L;
        Mockito.when(paintingRepositoryMock.findById(id)).thenReturn(Optional.empty());
        try {
            paintingService.getPaintingById(id);
        } catch (Exception e) {
            Assertions.assertEquals("Painting id does not exist", e.getMessage());
        }
    }


    @Test
    void whenSavedFromRepositoryThenFindsById() {
        Painting newPainting = new Painting();
        newPainting.setPaintingId(1L);
        paintingRepositoryMock.save(newPainting);
        Assertions.assertNotNull(paintingRepositoryMock.findById(newPainting.getPaintingId()));
    }


    @Test
    void whenSavedFromServiceThenFindsById() {
        Painting newPainting = new Painting();
        newPainting.setPaintingId(1L);
        paintingService.createPaintingWithoutAttachment(newPainting);
        Assertions.assertNotNull(paintingRepositoryMock.findById(newPainting.getPaintingId()));
    }

}