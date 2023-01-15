package com.novi.fassignment.services;

import com.novi.fassignment.controllers.dto.PaintingDto;
import com.novi.fassignment.controllers.dto.PaintingInputDto;
import com.novi.fassignment.models.Answer;
import com.novi.fassignment.models.Painting;
import org.springframework.data.domain.Sort;



import java.util.List;
import java.util.Optional;

public interface PaintingService {
    Sort.Direction getSortDirection(String direction);
    List<PaintingDto> getAllPaintings();
    List<Painting> getAllPaintingsByDescId();
    List<PaintingDto> getAllPaintingsByAscId();
    PaintingDto getPaintingById(Long id);
    Painting createPaintingWithoutAttachment(Painting painting);
    void updatePainting(PaintingInputDto dto, Painting painting);
    void createPainting(PaintingInputDto dto);
    void deletePaintingById(Long id);
    void deleteAllPaintings();
}