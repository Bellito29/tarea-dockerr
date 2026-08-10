package com.example.demo.security;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;

import com.example.demo.domain.Permission;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SecurityAuthority implements GrantedAuthority {

    private final Permission permission;

    @Override
    public @Nullable String getAuthority() {
        return permission.getName();
    }

}
