package com.nieghborapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "ANSWER")
@Data
public class Answer {
    //TODO make the ids as a randome string not just a number
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;


    private String content ;
    //TODO add date attribut and modified date

    @ManyToOne
    private User userId;

    @JsonIgnore
    @ManyToOne
    private Question questionId;


}
