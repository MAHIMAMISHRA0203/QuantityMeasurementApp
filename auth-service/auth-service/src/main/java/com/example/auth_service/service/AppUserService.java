package com.example.auth_service.service;

import com.example.auth_service.entity.AppUser;
import com.example.auth_service.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AppUserService {

    private final AppUserRepository repository;

    public AppUser getOrCreateUserFromGoogle(String email,
                                             String name, String picture) {
        return repository.findByEmail(email)
                .map(user -> {
                    user.setName(name);
                    user.setPicture(picture);
                    user.setProvider("google");
                    return repository.save(user);
                })
                .orElseGet(() -> repository.save(AppUser.builder()
                        .email(email)
                        .name(name)
                        .picture(picture)
                        .provider("google")
                        .role("USER")
                        .build()));
    }
}