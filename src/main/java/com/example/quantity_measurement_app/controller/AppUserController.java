package com.example.quantity_measurement_app.controller;

import com.example.quantity_measurement_app.dto.ApiResponseDTO;
import com.example.quantity_measurement_app.entity.AppUser;
import com.example.quantity_measurement_app.service.AppUserService;
import com.example.quantity_measurement_app.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AppUserController {

    private final AppUserService userService;
    private final JwtService jwtService;

    @GetMapping("/auth/token")
    public ResponseEntity<ApiResponseDTO<Map<String, Object>>> getAccessToken(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body(ApiResponseDTO.error(401, "User is not authenticated"));
        }

        String email = null, id = null, name = null, role = null;

        if (authentication.getPrincipal() instanceof OAuth2User oauth2User) {
            email = String.valueOf(oauth2User.getAttribute("email"));
            id = String.valueOf(oauth2User.getAttribute("id"));
            name = String.valueOf(oauth2User.getAttribute("name"));
            role = String.valueOf(oauth2User.getAttribute("role"));
        } else if (authentication instanceof JwtAuthenticationToken jwtAuth) {
            Jwt jwt = jwtAuth.getToken();
            email = jwt.getClaimAsString("email");
            id = jwt.getClaimAsString("id");
            name = jwt.getClaimAsString("name");
            role = jwt.getClaimAsString("role");
        }

        if (email == null) {
            return ResponseEntity.status(401).body(ApiResponseDTO.error(401, "Could not resolve user claims"));
        }

        String token = jwtService.generateToken(email, email, id, name, role);
        return ResponseEntity.ok(ApiResponseDTO.success(Map.of("accessToken", token), "Access token generated"));
    }

    @GetMapping("/users/me")
    public ResponseEntity<ApiResponseDTO<Map<String, Object>>> getCurrentUser(Authentication authentication) {
        try {
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(401).body(ApiResponseDTO.error(401, "User is not authenticated"));
            }

            String email = null;
            String id = null;
            String name = null;
            String picture = null;
            String role = null;

            if (authentication.getPrincipal() instanceof OAuth2User oauth2User) {
                email = safeString(oauth2User.getAttribute("email"));
                id = safeString(oauth2User.getAttribute("id"));
                name = safeString(oauth2User.getAttribute("name"));
                picture = safeString(oauth2User.getAttribute("picture"));
                role = safeString(oauth2User.getAttribute("role"));
            } else if (authentication instanceof JwtAuthenticationToken jwtAuth) {
                Jwt jwt = jwtAuth.getToken();
                email = safeString(jwt.getClaimAsString("email"));
                id = safeString(jwt.getClaimAsString("id"));
                name = safeString(jwt.getClaimAsString("name"));
                role = safeString(jwt.getClaimAsString("role"));
                picture = safeString(jwt.getClaimAsString("picture"));
            }

            if (email == null) {
                return ResponseEntity.status(401)
                        .body(ApiResponseDTO.error(401, "User properties are not available in token"));
            }

            Map<String, Object> payload = new HashMap<>();
            payload.put("id", id);
            payload.put("email", email);
            payload.put("name", name);
            payload.put("picture", picture);
            payload.put("role", role);

            return ResponseEntity.ok(ApiResponseDTO.success(payload, "Current user details"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                    ApiResponseDTO.error(500, "Error retrieving user: "
                            + (e.getMessage() != null ? e.getMessage() : e.getClass().getSimpleName())));
        }
    }

    @GetMapping("/users")
    public ResponseEntity<ApiResponseDTO<List<AppUser>>> listUsers() {
        return ResponseEntity.ok(ApiResponseDTO.success(userService.findAll(), "List of registered users"));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<ApiResponseDTO<Map<String, Object>>> deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.ok(ApiResponseDTO.success(Map.of("deletedId", id), "User deleted"));
    }


    private String safeString(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof String s) {
            return s;
        }
        if (value instanceof char[] chars) {
            return new String(chars);
        }
        return value.toString();
    }
}
