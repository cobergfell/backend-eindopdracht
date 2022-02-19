package com.novi.fassignment.services;

import com.novi.fassignment.controllers.dto.PaintingInputDto;
import com.novi.fassignment.models.Painting;

import java.util.List;
import java.util.Optional;

public interface PaintingService {
    List<Painting> getAllPaintings();
    List<Painting> getAllPaintingsByDescId();
    Painting getPaintingById(Long id);
    void updatePainting(PaintingInputDto dto, Painting painting);
    void createPainting(PaintingInputDto dto);
    void deletePaintingById(Long id);
    void deleteAllPaintings();
}