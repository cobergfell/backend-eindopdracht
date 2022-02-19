//Modified from https://github.com/bezkoder/spring-boot-upload-file-database.git

package com.novi.fassignment.repositories;

import com.novi.fassignment.models.FileStoredInDataBase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface  FileStorageInDataBaseRepository extends JpaRepository<FileStoredInDataBase, Long> {
    //@Modifying
    //@Query("select f from FileStoredInDataBase f where f.fileId = :questionId")//this syntax is correct
    //@Query("select f from FileStoredInDataBase f where f.questionId = :questionId")//this syntax is not correct error : could not resolve property: questionId of: com.novi.fassignment.models.FileStoredInDataBase
    //@Query("select * from FileStoredInDataBase f left join Question q where q.questionId = :questionId")//this syntax is not correct because of *
    //@Query("select f from FileStoredInDataBase f inner join Question q where q.questionId = :questionId")//this syntax is correct
    //@Query("select f from FileStoredInDataBase f inner join Question q on f. where q.questionId = :questionId")//this syntax, in JPQL is correct
    @Query(value="SELECT * FROM files_database INNER JOIN questions ON  files_database.question_id = questions.question_id;",nativeQuery = true)//this syntax is correct

    //@Query(value="select f from files_database f where f.questionId = :questionId",nativeQuery = true)//this syntax is correct
    List<FileStoredInDataBase> findByQuestionId(@Param("questionId") Long questionId);
}