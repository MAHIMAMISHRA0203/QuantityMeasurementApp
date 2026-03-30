package com.example.quantity_measurement_app.configuration;

import com.example.quantity_measurement_app.service.CustomOAuth2UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

        private final CustomOAuth2UserService customOAuth2UserService;
        private final JwtDecoder jwtDecoder;

        public WebSecurityConfig(CustomOAuth2UserService customOAuth2UserService,
                        JwtDecoder jwtDecoder) {
                this.customOAuth2UserService = customOAuth2UserService;
                this.jwtDecoder = jwtDecoder;
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                return http
                                .csrf(csrf -> csrf.disable())

                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))

                                .authorizeHttpRequests(auth -> auth
                                                // ✅ Public endpoints - no token needed
                                                .requestMatchers(
                                                                "/auth/**",
                                                                "/h2-console/**",
                                                                "/login/**",
                                                                "/oauth2/**",
                                                                "/measurements/**")
                                                .permitAll()

                                                // ✅ Protected endpoints - token required
                                                .requestMatchers(
                                                                "/users/**")
                                                .authenticated()

                                                .anyRequest().authenticated())

                                .oauth2Login(oauth2 -> oauth2
                                                .userInfoEndpoint(userInfo -> userInfo
                                                                .userService(customOAuth2UserService))
                                                .defaultSuccessUrl("/auth/oauth2/success", true)
                                                .failureUrl("/auth/oauth2/failure"))

                                .oauth2ResourceServer(oauth2 -> oauth2
                                                .jwt(jwt -> jwt
                                                                .decoder(jwtDecoder)
                                                                .jwtAuthenticationConverter(
                                                                                jwtAuthenticationConverter())))

                                .headers(headers -> headers
                                                .frameOptions(frame -> frame.disable()))

                                .build();
        }

        @Bean
        public JwtAuthenticationConverter jwtAuthenticationConverter() {
                JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
                grantedAuthoritiesConverter.setAuthoritiesClaimName("role");
                grantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");

                JwtAuthenticationConverter authConverter = new JwtAuthenticationConverter();
                authConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
                return authConverter;
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }
}