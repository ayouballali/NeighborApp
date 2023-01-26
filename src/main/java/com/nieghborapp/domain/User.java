package com.nieghborapp.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "USER")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id",nullable = false)
    private Long id ;

    private String name;

    private String username ;
    private String password ;

    @OneToMany
    private List<Answer> answers ;
    @OneToMany
    private List<Question> writedQuetions ;

    @OneToMany
    private List<Question> favoriteQuestions ;
    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<Role> roles ;
}
