package com.nieghborapp.domain;

import com.nieghborapp.domain.enums.RoleName;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "ROLE")
@Data
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    private RoleName roleName ;


}
