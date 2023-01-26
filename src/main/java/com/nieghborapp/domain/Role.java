package com.nieghborapp.domain;

import com.nieghborapp.domain.enums.RoleName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "ROLE")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role {
    public static final String USER = "USER";
    public static final String ADMIN = "ADMIN";

    public Role(String user) {
        name = user;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    private String name ;


}
