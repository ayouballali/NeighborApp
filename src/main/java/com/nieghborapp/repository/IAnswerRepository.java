package com.nieghborapp.repository;

import com.nieghborapp.domain.Answer;
import com.nieghborapp.domain.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IAnswerRepository extends JpaRepository<Answer,Long> {
    List<Answer> findAllByQuestionId(Question question);

}
