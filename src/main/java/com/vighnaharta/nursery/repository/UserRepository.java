package com.vighnaharta.nursery.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vighnaharta.nursery.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
