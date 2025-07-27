package org.tasksmanager.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;

    @GetMapping("/api/logout-success")
    public ResponseEntity<String> logoutSuccess() {
        return ResponseEntity.ok("You Signed Out");
    }

    @GetMapping("/token")
    public String getToken(@AuthenticationPrincipal OidcUser oidcUser) {
        OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(
                "cognito", oidcUser.getName());

        String accessToken = client.getAccessToken().getTokenValue();
        return accessToken;
    }
}
