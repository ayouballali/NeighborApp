package com.nieghborapp.domain;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "ANSWER")
@Data
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;


    private String content ;
    // add date atrribut and modifeyd date


}
