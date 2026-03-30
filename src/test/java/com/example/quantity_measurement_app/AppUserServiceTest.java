package com.example.quantity_measurement_app;

import com.example.quantity_measurement_app.entity.AppUser;
import com.example.quantity_measurement_app.service.AppUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class AppUserServiceTest {

    @Autowired
    private AppUserService appUserService;

    @Test
    public void testCreateOrUpdateFromGoogle() {
        AppUser user = appUserService.getOrCreateUserFromGoogle("test@example.com", "Test User",
                "http://example.com/img.png");
        assertThat(user).isNotNull();
        assertThat(user.getEmail()).isEqualTo("test@example.com");
        assertThat(user.getName()).isEqualTo("Test User");

        AppUser existing = appUserService.getOrCreateUserFromGoogle("test@example.com", "Test User 2",
                "http://example.com/img2.png");
        assertThat(existing.getId()).isEqualTo(user.getId());
        assertThat(existing.getName()).isEqualTo("Test User 2");
    }
}
