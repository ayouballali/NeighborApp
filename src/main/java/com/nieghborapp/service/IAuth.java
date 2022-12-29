package com.nieghborapp.service;

import com.nieghborapp.domain.User;

public interface IAuth {
    void signUp(User user);
    void signIn(User user);
}
