package com.nieghborapp.repository;

import com.nieghborapp.domain.Question;
import com.nieghborapp.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IQuestionRepository extends JpaRepository<Question,Long> {
    Optional<List<Question>> findAllByUser(User user);
    Optional <Question> findById(Long id);

}
