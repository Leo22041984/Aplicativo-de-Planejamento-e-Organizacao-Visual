package com.app.autismo.service;

import com.app.autismo.model.User;

public interface UserService {
    User saveUser(User user);
    User findByUsername(String username);
}

