package com.novi.fassignment.services;

import com.novi.fassignment.models.Painting;
import com.novi.fassignment.repositories.PaintingRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class PaintingServiceImplTest {

    // @InjectMocks vs @Mocks: see  https://howtodoinjava.com/mockito/mockito-mock-injectmocks/

//    @InjectMocks
//    private PaintingService paintingService;

    //@InjectMocks PaintingService paintingService = new PaintingService();
    @InjectMocks PaintingServiceImpl paintingService;

    @Mock
    private PaintingRepository paintingRepositoryMock;

    private Painting painting = new Painting();

    @Test
    void paintingServiceGetPaintingByIdTest() {
/*        Painting painting = new Painting();
        painting.setPaintingId(1L);*/
        Painting expected = new Painting();
        expected.setPaintingId(1L);
/*        Mockito.when(paintingService.getPaintingById(expected.getPaintingId()))
                .thenReturn(expected);*/
/*        Mockito.when(PaintingService.getPaintingById(1L))
                .thenReturn(expected);*/
        Mockito.when(paintingRepositoryMock.findById(1L))
                .thenReturn(Optional.of(expected));


        Painting actual = paintingService.getPaintingById(expected.getPaintingId());
        Assertions.assertSame(expected.getPaintingId(), actual.getPaintingId());
        //Assertions.assertSame(expected.getPaintingId(), painting.getPaintingId());
    }




    @Test
    void searchingUnknownIdShouldReturnErrorMessage() {
        long id = 1L;
        Mockito.when(paintingRepositoryMock.findById(id)).thenReturn(Optional.empty());
        try {
            paintingService.getPaintingById(id);
        } catch (Exception e) {
            Assertions.assertEquals("id does not exist", e.getMessage());
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