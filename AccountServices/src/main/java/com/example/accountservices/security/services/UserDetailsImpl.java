package com.example.accountservices.security.services;

import com.example.accountservices.entities.User;
import org.bson.types.ObjectId;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class UserDetailsImpl implements UserDetails {
    private static final long serialVersionUID = 1L;

    private ObjectId id;
    private String email;
    private String password;
    private GrantedAuthority authority;

    public UserDetailsImpl(ObjectId id, String email, String password, GrantedAuthority authority) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.authority = authority;
    }


    public static UserDetailsImpl build(User user) {

        GrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().getId().toString());
        return new UserDetailsImpl(
                user.getId(),
                user.getEmail(),
                user.getHashedPassword(),
                authority);
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(authority);
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    // Method to get role as string
    public String getRole() {
        return authority.getAuthority();
    }
}
