package org.tasksmanager.auth.service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class UserService {

    public String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            log.warn("No authentication found in security context.");
            throw new IllegalStateException("User not authenticated");
        }

        if (!(authentication instanceof OAuth2AuthenticationToken)) {
            log.warn("Authentication is not an instance of OAuth2AuthenticationToken: {}", authentication.getClass().getName());
            throw new IllegalStateException("Unexpected authentication type");
        }

        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;

        Object userIdObj = token.getPrincipal().getAttributes().get("sub");

        if (userIdObj == null) {
            log.error("User ID ('sub') not found in token attributes: {}", token.getPrincipal().getAttributes());
            throw new IllegalStateException("User ID not found in token");
        }

        String userId = userIdObj.toString();
        log.info("Retrieved user ID from token: {}", userId);
        return userId;
    }
}
