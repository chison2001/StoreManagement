package com.example.accountservices.entities;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "Account")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Account {
    @Id
    @Field("_id")
    private ObjectId id;
    @Field("userId")
    private String userId;
    private String type;
    private String provider;
    private String providerAccountId;
    private String refreshToken;
    private Integer expiresAt;
    private String tokenType;
    private String scope;
    private String idToken;
    private String sessionState;

    private User user;

}
