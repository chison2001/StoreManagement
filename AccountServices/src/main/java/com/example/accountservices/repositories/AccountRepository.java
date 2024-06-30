package com.example.accountservices.repositories;

import com.example.accountservices.entities.Account;
import com.example.accountservices.entities.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends MongoRepository<Account, ObjectId> {
    Optional<Account> findByRefreshToken(String refreshToken);

    int deleteByUser(User user);
}
