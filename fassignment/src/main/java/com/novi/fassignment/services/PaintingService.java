package com.novi.fassignment.services;

import com.novi.fassignment.controllers.dto.PaintingInputDto;
import com.novi.fassignment.models.Answer;
import com.novi.fassignment.models.Painting;

import java.util.List;
import java.util.Optional;

public interface PaintingService {
    List<Painting> getAllPaintings();
    List<Painting> getAllPaintingsByDescId();
    List<Painting> getAllPaintingsByAscId();
    Painting getPaintingById(Long id);
    Painting createPaintingWithoutAttachment(Painting painting);
    void updatePainting(PaintingInputDto dto, Painting painting);
    void createPainting(PaintingInputDto dto);
    void deletePaintingById(Long id);
    void deleteAllPaintings();
}