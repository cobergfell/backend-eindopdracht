package com.novi.fassignment.repositories;

import com.novi.fassignment.models.Painting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaintingRepository extends JpaRepository<Painting, Long> {
    Page<Painting> findByArtist(String artist, Pageable pageable);
    Page<Painting> findByTitleContaining(String title, Pageable pageable);
    List<Painting> findByTitleContaining(String title, Sort sort);
}
