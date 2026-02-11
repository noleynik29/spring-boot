package com.example.springboot.service.auth;

import com.example.springboot.dto.user.UserRegistrationRequestDto;
import com.example.springboot.dto.user.UserResponseDto;

public interface AuthenticationService {
    UserResponseDto register(UserRegistrationRequestDto request);
}
