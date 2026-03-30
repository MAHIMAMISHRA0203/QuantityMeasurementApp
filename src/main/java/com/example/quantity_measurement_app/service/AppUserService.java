package com.example.quantity_measurement_app.service;

import com.example.quantity_measurement_app.entity.AppUser;

import java.util.List;

public interface AppUserService {
    AppUser getOrCreateUserFromGoogle(String email, String name, String picture);

    AppUser findByEmail(String email);

    List<AppUser> findAll();

    void deleteById(Long id);
}
