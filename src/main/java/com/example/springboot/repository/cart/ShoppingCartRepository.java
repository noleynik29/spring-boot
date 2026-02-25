package com.example.springboot.repository.cart;

import com.example.springboot.entity.ShoppingCart;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart,Long> {
    Optional<ShoppingCart> findByUserId(Long userId);

    Optional<ShoppingCart> findByUserEmail(String email);
}
