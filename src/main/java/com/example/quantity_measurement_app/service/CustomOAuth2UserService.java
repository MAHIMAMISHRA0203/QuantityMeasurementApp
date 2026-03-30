package com.example.quantity_measurement_app.service;

import com.example.quantity_measurement_app.entity.AppUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final AppUserService appUserService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User user = super.loadUser(userRequest);

        Map<String, Object> attributes = user.getAttributes();
        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");
        String picture = (String) attributes.get("picture");

        if (email == null) {
            throw new OAuth2AuthenticationException("Google account has no email");
        }

        AppUser appUser = appUserService.getOrCreateUserFromGoogle(email, name, picture);

        Map<String, Object> attributesToUse = new java.util.HashMap<>();
        attributesToUse.put("id", appUser.getId());
        attributesToUse.put("email", appUser.getEmail());
        attributesToUse.put("name", appUser.getName() == null ? "" : appUser.getName());
        attributesToUse.put("picture", appUser.getPicture() == null ? "" : appUser.getPicture());
        attributesToUse.put("role", appUser.getRole() == null ? "USER" : appUser.getRole());

        return new DefaultOAuth2User(
                user.getAuthorities(),
                attributesToUse,
                "email");
    }
}
