package com.example.springboot.service.auth;

import com.example.springboot.dto.user.UserLoginRequestDto;
import com.example.springboot.dto.user.UserLoginResponseDto;
import com.example.springboot.dto.user.UserRegistrationRequestDto;
import com.example.springboot.dto.user.UserResponseDto;

public interface AuthenticationService {
    UserLoginResponseDto login(UserLoginRequestDto request);

    UserResponseDto register(UserRegistrationRequestDto request);
}
