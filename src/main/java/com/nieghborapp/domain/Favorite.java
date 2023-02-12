package com.nieghborapp.domain;

import lombok.Data;

import javax.persistence.*;

@Entity     @Data
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    //TODO ADD DATE WHEN IT HAS BEEN ADDED
    @ManyToOne
    private User user ;

    @ManyToOne
    private Question question;


}
