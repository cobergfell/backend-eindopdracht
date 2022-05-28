package com.novi.fassignment.repositories;

import com.novi.fassignment.models.NoviMethod1FileStoredOnDisk;
import com.novi.fassignment.models.Painting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.nio.file.Path;

public interface NoviMethod1FileUploadRepository extends JpaRepository<NoviMethod1FileStoredOnDisk, Long> {
    NoviMethod1FileStoredOnDisk findByLocation(String location);
}
