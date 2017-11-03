package com.example.repository;

import com.example.domain.User;

import java.util.Optional;

public interface UserRepository {

    Optional<User> findMostActive();

    Optional<User> findMostPopular();
}
