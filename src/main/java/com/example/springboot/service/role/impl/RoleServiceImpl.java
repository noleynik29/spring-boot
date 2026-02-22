package com.example.springboot.service.role.impl;

import com.example.springboot.entity.Role;
import com.example.springboot.repository.role.RoleRepository;
import com.example.springboot.service.role.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public Role findByName(Role.RoleName roleName) {
        return roleRepository.findByRole(roleName)
                .orElseThrow(() ->
                        new RuntimeException("Role " + roleName + " not found"));
    }
}
