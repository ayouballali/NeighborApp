package com.nieghborapp.repository;

import com.nieghborapp.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {
//    boolean existsByEmail(String email);
    Optional<User> findByUsername(String usernmae);
    boolean existsByUsername(String username);
    Optional<User> findByVerificationCode(String code );
}
