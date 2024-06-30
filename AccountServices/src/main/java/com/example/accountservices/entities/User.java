package com.example.accountservices.entities;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "User")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @Field("_id")
    private ObjectId id;
    private String name;
    private String email;
    private LocalDateTime emailVerified;
    private String image;
    private String hashedPassword;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<Account> accounts;

    private Role role;
}
