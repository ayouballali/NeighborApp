package com.nieghborapp;

import com.nieghborapp.domain.Role;
import com.nieghborapp.domain.User;
import com.nieghborapp.repository.IUserRepository;
import com.nieghborapp.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;

@SpringBootApplication @RequiredArgsConstructor
public class NieghborAppApplication implements CommandLineRunner {
    private  final  IUserRepository userRepo ;
    private final RoleRepository roleRepository;
    public static void main(String[] args) {
        SpringApplication.run(NieghborAppApplication.class, args);
    }
    @Override
    public void  run(String... strings){

//        if(!roleRepository.existsByName(Role.USER))roleRepository.save(new Role(Role.USER));
//        if(!roleRepository.existsByName(Role.ADMIN)) roleRepository.save(new Role(Role.ADMIN));
//
//        User student = new User();
//        student.setUsername("ayoub");
//        student.setPassword(new BCryptPasswordEncoder().encode("0000"));
//        student.setRoles(Arrays.asList(roleRepository.findByName(Role.USER).orElseThrow()));
//
//
//        if(!userRepo.existsByUsername(student.getUsername()))userRepo.save(student);
    }
}
