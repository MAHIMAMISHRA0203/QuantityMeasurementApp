package com.example.user_service.service;

import com.example.user_service.entity.AppUser;
import com.example.user_service.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AppUserService {

    private final AppUserRepository repository;

    public List<AppUser> findAll() {
        return repository.findAll();
    }

    public AppUser findByEmail(String email) {
        return repository.findByEmail(email).orElse(null);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    // ✅ Called when a user logs in via OAuth2 in auth-service
    // User service keeps its own copy of user data
    public AppUser getOrCreateUser(String email, String name,
                                   String picture, String role) {
        return repository.findByEmail(email)
                .map(user -> {
                    user.setName(name);
                    user.setPicture(picture);
                    return repository.save(user);
                })
                .orElseGet(() -> repository.save(AppUser.builder()
                        .email(email)
                        .name(name)
                        .picture(picture)
                        .provider("google")
                        .role(role != null ? role : "USER")
                        .build()));
    }
}