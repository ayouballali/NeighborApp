package com.nieghborapp.domain;

import lombok.Data;


import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "QUESTION")
@Data
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    private String title ;
    private String content ;
    private String location ;

    @OneToMany
    private List<Answer> answers ;


}