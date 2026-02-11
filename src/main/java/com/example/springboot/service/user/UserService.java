package com.example.springboot.service.user;

import com.example.springboot.entity.User;

public interface UserService {
    boolean existsByEmail(String email);

    User save(User user);
}
