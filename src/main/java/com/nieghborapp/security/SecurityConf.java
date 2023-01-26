package com.nieghborapp.security;

import com.nieghborapp.domain.Role;
import com.nieghborapp.filters.CustomAuthorizationFilter;
import com.nieghborapp.filters.CustomeAuthentificationFilter;
import com.nieghborapp.service.IUserService;
import com.nieghborapp.service.impl.UserService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;

@Configuration @RequiredArgsConstructor
public class SecurityConf {

    private final   UserService userservice ;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder() ;

   // authmanger
    @Bean
    public  AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
       return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userservice)
                .passwordEncoder(passwordEncoder)
                .and()
                .build();

    }

    // filter chain
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity, AuthenticationManager authenticationManager) throws Exception {

        CustomeAuthentificationFilter customeAuthentificationFilter = new CustomeAuthentificationFilter(authenticationManager);
        customeAuthentificationFilter.setFilterProcessesUrl("/api/login");

        httpSecurity
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/login")
                .permitAll()
                .antMatchers("/api/user")
                .hasRole(Role.USER)
                .antMatchers("/api/admin")
                .hasRole(Role.ADMIN)
                .antMatchers("/")
                .authenticated()
                .and()
                .httpBasic()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

                httpSecurity.addFilter(customeAuthentificationFilter);
                httpSecurity.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

}
