package com.nieghborapp.security;

import com.nieghborapp.domain.Role;
import com.nieghborapp.filters.CustomAuthenticationfailure;
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
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.servlet.FilterChain;

@Configuration  @EnableGlobalMethodSecurity(prePostEnabled = true) @EnableWebSecurity
public class SecurityConf {
    @Autowired
    private    UserService userservice ;
    @Autowired
    private  PasswordEncoder passwordEncoder1 ;
//    @Autowired
//    private CustomAuthenticationfailure customAuthenticationfailure;
//    @Autowired ExceptionHandlingFilter exceptionHandlingFilter;

   // authmanger
    @Bean
    public  AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
       return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userservice)
                .passwordEncoder(passwordEncoder1)
                .and()
                .build();

    }

    @Bean
    public static PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    // filter chain
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity, AuthenticationManager authenticationManager) throws Exception {

        CustomeAuthentificationFilter customeAuthentificationFilter = new CustomeAuthentificationFilter(authenticationManager);
        customeAuthentificationFilter.setAuthenticationFailureHandler(customAuthenticationfailure());
        customeAuthentificationFilter.setFilterProcessesUrl("/api/login");


        httpSecurity
                .csrf().disable()
                .cors()
                .and()
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

    @Bean
    public AuthenticationFailureHandler customAuthenticationfailure(){
        return new CustomAuthenticationfailure();
    }
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://localhost:3000");
        config.addAllowedHeader("*");
        config.addAllowedMethod("GET");
        config.addAllowedMethod("POST");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }


}
