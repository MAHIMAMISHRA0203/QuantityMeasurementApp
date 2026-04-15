package com.example.user_service.controller;

import com.example.user_service.entity.AppUser;
import com.example.user_service.service.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final AppUserService userService;

    // ✅ Get current logged in user info from JWT token
    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> getCurrentUser(
            Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401)
                    .body(Map.of("error", "Not authenticated"));
        }

        String email = null, id = null, name = null, role = null;

        if (authentication instanceof JwtAuthenticationToken jwtAuth) {
            Jwt jwt = jwtAuth.getToken();
            email = jwt.getClaimAsString("email");
            id    = jwt.getClaimAsString("id");
            name  = jwt.getClaimAsString("name");
            role  = jwt.getClaimAsString("role");
        }

        if (email == null) {
            return ResponseEntity.status(401)
                    .body(Map.of("error", "Could not resolve user"));
        }

        // ✅ Get or create user in user-service database
        AppUser user = userService.getOrCreateUser(email, name, null, role);

        Map<String, Object> response = new HashMap<>();
        response.put("id",    user.getId());
        response.put("email", user.getEmail());
        response.put("name",  user.getName());
        response.put("role",  user.getRole());

        return ResponseEntity.ok(response);
    }

    // ✅ Get all users
    @GetMapping
    public ResponseEntity<List<AppUser>> getAllUsers() {
        return ResponseEntity.ok(userService.findAll());
    }

    // ✅ Delete user by id
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteUser(
            @PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "deletedId", id
        ));
    }
}