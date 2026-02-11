package com.example.springboot.service.auth.impl;

import com.example.springboot.dto.user.UserRegistrationRequestDto;
import com.example.springboot.dto.user.UserResponseDto;
import com.example.springboot.entity.User;
import com.example.springboot.exception.RegistrationException;
import com.example.springboot.mapper.UserMapper;
import com.example.springboot.service.auth.AuthenticationService;
import com.example.springboot.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserService userService;
    private final UserMapper userMapper;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto request) {
        if (userService.existsByEmail(request.getEmail())) {
            throw new RegistrationException("Email already register");
        }
        User user = userMapper.toUser(request);
        userService.save(user);
        return userMapper.toDto(user);
    }
}
