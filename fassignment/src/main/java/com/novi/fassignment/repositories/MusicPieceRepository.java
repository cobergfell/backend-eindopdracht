package com.novi.fassignment.repositories;

import com.novi.fassignment.models.MusicPiece;
import com.novi.fassignment.models.Painting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MusicPieceRepository extends JpaRepository<MusicPiece, Long> {
    Page<MusicPiece> findByArtist(String artist, Pageable pageable);
    Page<MusicPiece> findByTitleContaining(String title, Pageable pageable);
    List<MusicPiece> findByTitleContaining(String title, Sort sort);
}
