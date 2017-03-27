package com.example.harness;

import com.example.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TestUserRepository extends MongoRepository<User, String> {
}
