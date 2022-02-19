package com.novi.fassignment.repositories;

import com.novi.fassignment.models.QuestionWithFilesStoredOnDisc;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionWithFilesStoredOnDiscRepository extends JpaRepository<QuestionWithFilesStoredOnDisc, Long> {
}

