package com.example.accountservices.repositories;

import com.example.accountservices.entities.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, ObjectId> {
    Optional<User> findById(String id);

    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);
}
