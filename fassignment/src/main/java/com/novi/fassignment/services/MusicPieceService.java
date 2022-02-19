package com.novi.fassignment.services;

import com.novi.fassignment.controllers.dto.MusicPieceInputDto;
import com.novi.fassignment.models.MusicPiece;

import java.util.List;
import java.util.Optional;

public interface MusicPieceService {
    List<MusicPiece> getAllMusicPieces();
    List<MusicPiece> getAllMusicPiecesByDescId();
    MusicPiece getMusicPieceById(Long id);
    void updateMusicPiece(MusicPieceInputDto dto, MusicPiece musicPiece);
    void createMusicPiece(MusicPieceInputDto dto);
    void deleteMusicPieceById(Long id);
    void deleteAllMusicPieces();
}