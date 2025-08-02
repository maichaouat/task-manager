package org.tasksmanager.auth.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;
import org.tasksmanager.auth.handler.CognitoLogoutHandler;
import org.tasksmanager.auth.service.UserService;

/**
 * Class to configure AWS Cognito as an OAuth 2.0 authorizer with Spring Security.
 * In this configuration, we specify our OAuth Client.
 * We also declare that all requests must come from an authenticated user.
 * Finally, we configure our logout handler.
 */
@Slf4j
@EnableWebSecurity
@Configuration
@Profile("!test")
public class SecurityConfiguration {

    @Bean
    public CognitoLogoutHandler cognitoLogoutHandler(
            @Value("${spring.application.cognito.logout-redirect-uri}") String logoutRedirectUri,
            @Value("${spring.security.oauth2.client.registration.cognito.client-id}") String clientId
    ) {
        return new CognitoLogoutHandler(logoutRedirectUri, clientId);
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, CognitoLogoutHandler cognitoLogoutHandler) throws Exception {

        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers(
                                "/", "/login", "/error",
                                "/v3/api-docs/**",
                                "/v3/api-docs",
                                "/swagger-resources/**",
                                "/webjars/**"
                        ).permitAll()
                        .requestMatchers("/api/**", "/projects.html").authenticated()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .successHandler((request, response, authentication) -> {
                            OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
                            String username = token.getPrincipal().getAttributes().get("username").toString();
                            log.info("User '{}' logged in", username);

                        })
                        .defaultSuccessUrl("/", true)
                )
                .logout(logout -> logout.logoutSuccessHandler(cognitoLogoutHandler));

        return http.build();
    }
}