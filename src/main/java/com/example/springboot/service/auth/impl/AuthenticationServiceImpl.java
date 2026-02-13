package com.example.springboot.service.auth.impl;

import com.example.springboot.dto.user.UserRegistrationRequestDto;
import com.example.springboot.dto.user.UserResponseDto;
import com.example.springboot.entity.User;
import com.example.springboot.exception.RegistrationException;
import com.example.springboot.mapper.UserMapper;
import com.example.springboot.repository.user.UserRepository;
import com.example.springboot.service.auth.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RegistrationException("Email " + request.getEmail() + " already exists");
        }
        User user = userMapper.toUser(request);
        return userMapper.toDto(userRepository.save(user));
    }
}
