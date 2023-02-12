package com.nieghborapp.service.impl;

import com.nieghborapp.domain.Favorite;
import com.nieghborapp.domain.Question;
import com.nieghborapp.domain.User;
import com.nieghborapp.exceptions.NotFoundException;
import com.nieghborapp.repository.IFavoriteRepository;
import com.nieghborapp.repository.IQuestionRepository;
import com.nieghborapp.repository.IUserRepository;
import com.nieghborapp.service.IQuestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service @Slf4j @RequiredArgsConstructor @Transactional
public class QuestionService implements IQuestionService {
    private final IQuestionRepository questionRepository;
    private final IUserRepository userRepository;
    private final IFavoriteRepository favoriteRepository ;

    @Override
    public void createQuestion(Authentication authentication, Question question) {
        log.info("creaete post service ");


        String currentUsername = authentication.getName();
        User currentUser =  userRepository.findByUsername(currentUsername).orElseThrow();
        question.setUser(currentUser);

        questionRepository.save(question);

        // TODO SET THE DATES
    }

    @Override
    public void deleteQuestion(Authentication authentication, Long id) throws Exception {

        Question questionToDelete = questionRepository.findById(id).orElseThrow(()-> new NotFoundException("this post is not found "));

        User loggedUser = userRepository.findByUsername(authentication.getName()).orElseThrow(()-> new NotFoundException("someting went woring "));
        if(loggedUser.getId().equals(questionToDelete.getUser().getId())){
            questionRepository.deleteById(id);
            log.info("the question was deleted successfully ");
            return;
        }
        throw new Exception("tou don't have permision to do that");
    }

    @Override
    public Question getQuetion(Authentication authentication, Long id) throws NotFoundException {
        Question question =    questionRepository.findById(id).orElseThrow( () -> new NotFoundException("there' is no question with that id "));

        return question;
    }



    @Override
    public Question updateQuestion(Authentication authentication, Long id, Question question ) throws Exception {
        Question questionToUpdate = questionRepository.findById(id).orElseThrow(()-> new NotFoundException("this post is not found "));

        User loggedUser = userRepository.findByUsername(authentication.getName()).orElseThrow(()-> new NotFoundException("someting went woring "));
        if(loggedUser.getId().equals(questionToUpdate.getUser().getId())){
            questionToUpdate.setContent(question.getContent());
            questionToUpdate.setTitle(question.getTitle());
            questionRepository.save(questionToUpdate);
            log.info("the question was updated fully ");
            return questionToUpdate;
        }
        log.error("error was here in the delete question service ");
        throw new Exception("you don't have permission to do that");

    }

    @Override
    public void addToFavorite(Authentication authentication, Long postId) throws NotFoundException {
        Question questionToFavorite = questionRepository.findById(postId).orElseThrow(()->new NotFoundException("this post wasn't found "));

        User loggedUser = userRepository.findByUsername(authentication.getName()).orElseThrow(()->new NotFoundException("something went wrong "));
        Favorite favorite = new Favorite();
        favorite.setQuestion(questionToFavorite);
        favorite.setUser(loggedUser);
        favoriteRepository.save(favorite);
        
    }
    @Override
    public List<Question> getFavoriteByUser (Authentication authentication) throws NotFoundException {
        User loggedUser = userRepository.findByUsername(authentication.getName()).orElseThrow(()->new NotFoundException("something went wrong "));
        return favoriteRepository.findAllByUser(loggedUser).stream().map((favorite -> favorite.getQuestion())).collect(Collectors.toList());

    }

    @Override
    public void deleteFavorite(Authentication authentication, Long id) throws NotFoundException {
        User loggedUser = userRepository.findByUsername(authentication.getName()).orElseThrow(()->new NotFoundException("something went wrong "));
        Question questionToDelete = questionRepository.findById(id).orElseThrow(()->new NotFoundException("the question doesn't exist"));
        favoriteRepository.deleteByQuestion(questionToDelete);
    }


}
