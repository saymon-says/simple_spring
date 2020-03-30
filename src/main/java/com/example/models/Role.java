package com.example.models;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    USER,
    ADMIN,
    REDACTOR;

    @Override
    public String getAuthority() {
        return name();
    }
}
