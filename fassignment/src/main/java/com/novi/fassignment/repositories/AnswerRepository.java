package com.novi.fassignment.repositories;
import com.novi.fassignment.models.Answer;
import com.novi.fassignment.models.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
}