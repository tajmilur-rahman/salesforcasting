package com.example.application.data.service;

import com.example.application.data.entity.UsersDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserDetailsServiceNew {

    private final UserDetailsRepository repository;



    @Autowired
    public UserDetailsServiceNew(UserDetailsRepository repository) {
        this.repository = repository;
    }

    public UsersDetails findByID(String userid){
        return repository.findByUserId(userid);
    }

    public Optional<UsersDetails> get(UUID id) {
        return repository.findById(id);
    }

    public UsersDetails update(UsersDetails entity) {
        return repository.save(entity);
    }

    public void delete(UUID id) {
        repository.deleteById(id);
    }

    public Page<UsersDetails> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public int count() {
        return (int) repository.count();
    }

}
