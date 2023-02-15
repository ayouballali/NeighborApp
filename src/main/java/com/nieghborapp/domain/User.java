package com.nieghborapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;
//TODO  ADD THE CTRATED DATE AND UPDATED DATE
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
    @JsonIgnore
    private String password ;

    private boolean isEnabled ;
    @JsonIgnore
    private String  verificationCode ;
    private String email;





    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<Role> roles = new ArrayList<>() ;

    public void addRole(Role role){
        roles.add(role);
    }


    @JsonIgnore

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return null;
    }
    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return isEnabled;
    }
    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return isEnabled;
    }
    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return isEnabled;
    }
}
