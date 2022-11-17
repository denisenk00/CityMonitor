package com.denysenko.citymonitorweb.models.domain.security;

import com.denysenko.citymonitorweb.enums.UserAccountStatus;
import com.denysenko.citymonitorweb.models.entities.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;


public class SecurityUser implements UserDetails {

    private User user;

    public SecurityUser (User user){
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRole().getAuthorities();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
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
        return !user.getUserAccountStatus().equals(UserAccountStatus.NOT_ACTIVE);
    }
}
