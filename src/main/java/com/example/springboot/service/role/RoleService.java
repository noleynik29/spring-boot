package com.example.springboot.service.role;

import com.example.springboot.entity.Role;

public interface RoleService {
    Role findByName(Role.RoleName roleName);
}
