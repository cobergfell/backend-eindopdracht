//Modified from https://github.com/bezkoder/spring-boot-upload-file-database.git

package com.novi.fassignment.repositories;

import com.novi.fassignment.models.FileStoredInDataBase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface  FileStorageInDataBaseRepository extends JpaRepository<FileStoredInDataBase, Long> {
 @Query(value="SELECT * FROM files_database INNER JOIN questions ON  files_database.question_id = questions.question_id;",nativeQuery = true)//this syntax is correct
    List<FileStoredInDataBase> findByQuestionId(@Param("questionId") Long questionId);
}