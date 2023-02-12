package com.nieghborapp.repository;

import com.nieghborapp.domain.Favorite;
import com.nieghborapp.domain.Question;
import com.nieghborapp.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IFavoriteRepository extends JpaRepository<Favorite,Long> {

    public List<Favorite> findAllByUser(User user);
    public void deleteByQuestion(Question question);
}
