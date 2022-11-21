package com.example.application.security;

import com.example.application.data.entity.UsersDetails;
import com.example.application.data.service.UserDetailsRepository;
import com.example.application.data.service.UserDetailsServiceNew;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    UserDetailsRepository userDetailsServiceNew;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UsersDetails user = userDetailsServiceNew.findByUserId(username);
        if(user==null){
            throw new UsernameNotFoundException("User With User ID:"+username+" Not Found!");
        }
        return new CustomUserDetails(user);
    }
}
