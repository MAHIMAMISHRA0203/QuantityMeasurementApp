package com.example.quantity_measurement_app.controller;

import com.example.quantity_measurement_app.dto.ApiResponseDTO;
import com.example.quantity_measurement_app.service.AppUserService;
import com.example.quantity_measurement_app.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AppUserService userService;
    private final JwtService jwtService;

    public AuthController(AppUserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    // ✅ OAuth2 Google login success - Spring redirects here after Google login
    @GetMapping("/oauth2/success")
    public ResponseEntity<ApiResponseDTO<Map<String, Object>>> oauth2Success(
            @AuthenticationPrincipal OAuth2User oauth2User) {

        // ✅ Check if user is authenticated
        if (oauth2User == null) {
            return ResponseEntity.status(401)
                    .body(ApiResponseDTO.error(401, "Not authenticated"));
        }

        // ✅ Read from YOUR custom attributes map (set in CustomOAuth2UserService)
        // "sub" does NOT exist in your map — use "email" and "id" instead
        String email = oauth2User.getAttribute("email");
        String name = oauth2User.getAttribute("name");

        // ✅ Get id safely — appUser.getId() is a Long, convert to String
        Object idObj = oauth2User.getAttribute("id");
        String id = idObj != null ? idObj.toString() : email;

        // ✅ Validate email
        if (email == null || email.isBlank()) {
            return ResponseEntity.status(400)
                    .body(ApiResponseDTO.error(400, "Email is required"));
        }

        // ✅ Generate JWT token — subject is email (never null)
        String token = jwtService.generateToken(email, email, id, name, "USER");

        // ✅ Build response
        Map<String, Object> responseData = Map.of(
                "token", token,
                "email", email,
                "name", name != null ? name : "",
                "id", id);

        return ResponseEntity.ok(ApiResponseDTO.success(responseData, "OAuth2 login successful"));
    }

    // ✅ OAuth2 failure endpoint
    @GetMapping("/oauth2/failure")
    public ResponseEntity<ApiResponseDTO<Map<String, Object>>> oauth2Failure() {
        return ResponseEntity.status(401)
                .body(ApiResponseDTO.error(401, "OAuth2 login failed. Please try again."));
    }
}