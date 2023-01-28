package com.nieghborapp.domain;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "USER")
@Data
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id",nullable = false)
    private Long id ;

    private String name;

    private String username ;
    private String password ;

    private boolean isEnabled ;
    private String  verificationCode ;
    private String email;

    @OneToMany
    private List<Answer> answers ;
    @OneToMany
    private List<Question> writedQuetions ;

    @OneToMany
    private List<Question> favoriteQuestions ;
    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<Role> roles = new ArrayList<>() ;

    public void addRole(Role role){
        roles.add(role);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isEnabled;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isEnabled;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isEnabled;
    }
}
