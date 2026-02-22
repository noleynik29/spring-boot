package com.example.springboot.service.auth.impl;

import com.example.springboot.dto.user.UserLoginRequestDto;
import com.example.springboot.dto.user.UserLoginResponseDto;
import com.example.springboot.dto.user.UserRegistrationRequestDto;
import com.example.springboot.dto.user.UserResponseDto;
import com.example.springboot.entity.Role;
import com.example.springboot.entity.User;
import com.example.springboot.exception.RegistrationException;
import com.example.springboot.mapper.UserMapper;
import com.example.springboot.repository.user.UserRepository;
import com.example.springboot.service.auth.AuthenticationService;
import com.example.springboot.service.cart.ShoppingCartService;
import com.example.springboot.service.role.RoleService;
import com.example.springboot.util.JwtUtil;
import jakarta.transaction.Transactional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final JwtUtil jwtUtil;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final ShoppingCartService shoppingCartService;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RegistrationException("Email " + request.getEmail() + " already exists");
        }
        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        Role role = roleService.findByName(Role.RoleName.USER);
        user.setRoles(Set.of(role));
        shoppingCartService.createCartForUser(user);
        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public UserLoginResponseDto login(UserLoginRequestDto request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        String token = jwtUtil.generateToken(authentication.getName());
        return new UserLoginResponseDto(token);
    }
}
