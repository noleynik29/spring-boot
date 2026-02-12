package com.example.springboot.repository.user;

import com.example.springboot.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean findByEmail(String username);
}
