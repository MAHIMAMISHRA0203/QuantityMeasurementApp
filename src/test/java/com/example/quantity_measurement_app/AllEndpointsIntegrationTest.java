package com.example.quantity_measurement_app;

import com.example.quantity_measurement_app.entity.AppUser;
import com.example.quantity_measurement_app.repository.AppUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("Full endpoint integration tests")
public class AllEndpointsIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AppUserRepository appUserRepository;

    @BeforeEach
    void cleanup() {
        appUserRepository.deleteAll();
    }

    @Test
    @DisplayName("Measurement compare endpoint should succeed publicly")
    void comparePublicEndpoint() throws Exception {
        String payload = "{\"value1\":1,\"unit1\":\"FEET\",\"value2\":12,\"unit2\":\"INCH\",\"measurementType\":\"LENGTH\"}";

        mockMvc.perform(post("/measurements/compare")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.equal", is(true)));
    }

    @Test
    @DisplayName("Measurement history should be authenticated")
    void historyRequiresAuthentication() throws Exception {
        mockMvc.perform(get("/measurements/history"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Google-auth-protected endpoints with mock OAuth2 user")
    void usersApiAuthenticated() throws Exception {
        // Create an AppUser in DB so responses are not empty
        AppUser user = AppUser.builder().email("mock@user.com").name("Mock User").role("USER").provider("google")
                .build();
        appUserRepository.save(user);

        DefaultOAuth2User oauth2User = new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                Map.of(
                        "id", "12345",
                        "email", "mock@user.com",
                        "name", "Mock User",
                        "picture", "http://example.com/p.png",
                        "role", "USER"),
                "email");

        mockMvc.perform(get("/users/me").with(oauth2Login().oauth2User(oauth2User)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.email", is("mock@user.com")));
    }

    @Test
    @DisplayName("Current user info should be accessible with JWT bearer token")
    void usersMeWithJwtBearer() throws Exception {
        mockMvc.perform(get("/users/me").with(jwt().jwt(jwt -> {
            jwt.claim("email", "mock@user.com");
            jwt.claim("id", "12345");
            jwt.claim("name", "Mock User");
            jwt.claim("role", "USER");
        })))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.email", is("mock@user.com")));
    }

    @Test
    @DisplayName("Current user info should return unauthorized when not authenticated")
    void usersMeUnauthorized() throws Exception {
        mockMvc.perform(get("/api/users/me"))
                .andExpect(status().isUnauthorized());
    }

}
