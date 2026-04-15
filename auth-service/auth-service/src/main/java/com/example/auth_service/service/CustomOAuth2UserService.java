package com.example.auth_service.service;

import com.example.auth_service.entity.AppUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final AppUserService appUserService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest)
            throws OAuth2AuthenticationException {
        OAuth2User user = super.loadUser(userRequest);
        Map<String, Object> attributes = user.getAttributes();

        String email   = (String) attributes.get("email");
        String name    = (String) attributes.get("name");
        String picture = (String) attributes.get("picture");

        if (email == null) {
            throw new OAuth2AuthenticationException("Google account has no email");
        }

        AppUser appUser = appUserService
                .getOrCreateUserFromGoogle(email, name, picture);

        Map<String, Object> attrs = new HashMap<>();
        attrs.put("id",      appUser.getId());
        attrs.put("email",   appUser.getEmail());
        attrs.put("name",    appUser.getName()    != null ? appUser.getName()    : "");
        attrs.put("picture", appUser.getPicture() != null ? appUser.getPicture() : "");
        attrs.put("role",    appUser.getRole()    != null ? appUser.getRole()    : "USER");

        return new DefaultOAuth2User(user.getAuthorities(), attrs, "email");
    }
}