package com.denysenko.citymonitorweb.enums;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

public enum UserRole {
    SUPER_ADMIN(Permission.getAll()),

    ADMIN(Set.of(Permission.APPEALS_WRITE, Permission.APPEALS_READ,
                Permission.LAYOUTS_WRITE, Permission.LAYOUTS_READ,
                Permission.QUIZZES_WRITE, Permission.QUIZZES_READ)),

    VIEWER(Set.of(Permission.APPEALS_READ, Permission.LAYOUTS_READ, Permission.QUIZZES_READ));

    private Set<Permission> permissions;

    UserRole(Set<Permission> permissions){
        this.permissions = permissions;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getAuthorities() {
        return getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getTitle()))
                .collect(Collectors.toSet());
    }
}