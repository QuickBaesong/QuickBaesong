package com.qb.authservice.domain.repository;

import com.qb.authservice.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);
    Optional<User> findByUserId(UUID uuid);

}
