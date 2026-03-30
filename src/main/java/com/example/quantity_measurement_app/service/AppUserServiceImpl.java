package com.example.quantity_measurement_app.service;

import com.example.quantity_measurement_app.entity.AppUser;
import com.example.quantity_measurement_app.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AppUserServiceImpl implements AppUserService {

    private final AppUserRepository repository;

    @Override
    public AppUser getOrCreateUserFromGoogle(String email, String name, String picture) {
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

    @Override
    public AppUser findByEmail(String email) {
        return repository.findByEmail(email).orElse(null);
    }

    @Override
    public List<AppUser> findAll() {
        return repository.findAll();
    }

    @Override
    public void deleteById(Long id) {

        repository.deleteById(id);
    }
}
