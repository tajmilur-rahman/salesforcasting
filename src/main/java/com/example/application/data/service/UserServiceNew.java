package com.example.application.data.service;


import com.example.application.data.entity.UsersDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;

@Service
@Transactional
public class UserServiceNew implements UserDetailsService {

    @Autowired
    private UserDetailsRepository repository;

    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException{
        UsersDetails user = repository.findByUserId(userId);
        if(user==null){
            throw new UsernameNotFoundException("No user found with username: " + userId);
        }
        boolean enabled = true;
        boolean accountNonExpired = true;
        boolean credentialsNonExpired = true;
        boolean accountNonLocked = true;

        return new User(
                user.getEmail(), user.getPassword(), enabled, accountNonExpired,
                credentialsNonExpired, accountNonLocked, Collections.singleton(new SimpleGrantedAuthority("ADMIN")));

    }
}
