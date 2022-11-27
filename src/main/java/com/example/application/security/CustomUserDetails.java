package com.example.application.security;

import com.example.application.data.entity.UsersDetails;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class CustomUserDetails implements UserDetails {

    UsersDetails usersDetails;

    CustomUserDetails(UsersDetails usersDetails){
        this.usersDetails = usersDetails;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return usersDetails.getPassword();
    }

    public String getFname(){
        return usersDetails.getFirstName();
    }

    public String getLname(){
        return usersDetails.getLastName();
    }

    public String getEmail(){
        return usersDetails.getEmail();
    }

    @Override
    public String getUsername() {
        return usersDetails.getUserId();
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
}
