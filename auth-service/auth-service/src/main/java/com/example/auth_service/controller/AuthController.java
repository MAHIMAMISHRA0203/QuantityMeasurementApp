package com.example.auth_service.controller;

import com.example.auth_service.entity.AppUser;
import com.example.auth_service.repository.AppUserRepository;
import com.example.auth_service.service.JwtService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtService jwtService;
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(JwtService jwtService,
                          AppUserRepository appUserRepository,
                          PasswordEncoder passwordEncoder) {
        this.jwtService = jwtService;
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/signup")
    public ResponseEntity<Map<String, Object>> signup(@RequestBody SignupRequest request) {
        if (request == null || isBlank(request.email()) || isBlank(request.password()) || isBlank(request.name())) {
            return ResponseEntity.badRequest().body(error("Please provide name, email, and password."));
        }

        String normalizedEmail = request.email().trim().toLowerCase();
        if (appUserRepository.findByEmail(normalizedEmail).isPresent()) {
            return ResponseEntity.badRequest().body(error("Email already registered."));
        }

        AppUser user = AppUser.builder()
                .email(normalizedEmail)
                .name(request.name().trim())
                .role("USER")
                .provider("local")
                .password(passwordEncoder.encode(request.password()))
                .build();
        AppUser saved = appUserRepository.save(user);

        String token = jwtService.generateToken(
                saved.getEmail(),
                saved.getEmail(),
                String.valueOf(saved.getId()),
                saved.getName(),
                saved.getRole() != null ? saved.getRole() : "USER"
        );

        return ResponseEntity.ok(success(saved, token, "Signup successful"));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequest request) {
        if (request == null || isBlank(request.email()) || isBlank(request.password())) {
            return ResponseEntity.badRequest().body(error("Please provide email and password."));
        }

        String normalizedEmail = request.email().trim().toLowerCase();
        Optional<AppUser> userOpt = appUserRepository.findByEmail(normalizedEmail);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(401).body(error("Invalid email or password."));
        }

        AppUser user = userOpt.get();
        if (isBlank(user.getPassword()) || !passwordEncoder.matches(request.password(), user.getPassword())) {
            return ResponseEntity.status(401).body(error("Invalid email or password."));
        }

        String token = jwtService.generateToken(
                user.getEmail(),
                user.getEmail(),
                String.valueOf(user.getId()),
                user.getName(),
                user.getRole() != null ? user.getRole() : "USER"
        );

        return ResponseEntity.ok(success(user, token, "Login successful"));
    }

    // ✅ OAuth2 success - redirects to Angular frontend with token
    @GetMapping("/oauth2/success")
    public void oauth2Success(
            @AuthenticationPrincipal OAuth2User oauth2User,
            HttpServletResponse response) throws IOException {

        if (oauth2User == null) {
            response.sendRedirect(
                    "http://localhost:4200/oauth-callback?error=not_authenticated");
            return;
        }

        String email = oauth2User.getAttribute("email");
        String name  = oauth2User.getAttribute("name");
        Object idObj = oauth2User.getAttribute("id");
        String id    = idObj != null ? idObj.toString() : email;

        if (email == null || email.isBlank()) {
            response.sendRedirect(
                    "http://localhost:4200/oauth-callback?error=no_email");
            return;
        }

        // ✅ Generate JWT token
        String token = jwtService.generateToken(email, email, id, name, "USER");

        // ✅ Redirect to Angular frontend with token as query params
        String redirectUrl = "http://localhost:4200/oauth2-callback"
                + "?token=" + URLEncoder.encode(token, StandardCharsets.UTF_8)
                + "&email=" + URLEncoder.encode(email, StandardCharsets.UTF_8)
                + "&name=" + URLEncoder.encode(name != null ? name : "", StandardCharsets.UTF_8)
                + "&id=" + URLEncoder.encode(id, StandardCharsets.UTF_8);

        response.sendRedirect(redirectUrl);
    }

    // ✅ OAuth2 failure - redirects to Angular frontend with error
    @GetMapping("/oauth2/failure")
    public void oauth2Failure(HttpServletResponse response) throws IOException {
        response.sendRedirect(
                "http://localhost:4200/oauth-callback?error=oauth2_failed");
    }

    private Map<String, Object> success(AppUser user, String token, String message) {
        return Map.of(
                "success", true,
                "message", message,
                "data", Map.of(
                        "token", token,
                        "email", user.getEmail() != null ? user.getEmail() : "",
                        "name", user.getName() != null ? user.getName() : "",
                        "id", String.valueOf(user.getId())
                )
        );
    }

    private Map<String, Object> error(String message) {
        return Map.of(
                "success", false,
                "message", message
        );
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    public record SignupRequest(@NotBlank String name, @Email @NotBlank String email, @NotBlank String password) {}
    public record LoginRequest(@Email @NotBlank String email, @NotBlank String password) {}
}
