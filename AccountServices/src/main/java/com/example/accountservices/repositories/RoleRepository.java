package com.example.accountservices.repositories;

import com.example.accountservices.entities.Role;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends MongoRepository<Role, ObjectId> {
    Role findByName(String name);
}
