package org.tasksmanager.auth.handler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;

/**
 * Cognito has a custom logout url.
 * See more information <a href="https://docs.aws.amazon.com/cognito/latest/developerguide/logout-endpoint.html">here</a>.
 */
public class CognitoLogoutHandler extends SimpleUrlLogoutSuccessHandler {

    /**
     * The domain of your user pool.
     */
    private String domain = "https://eu-north-1abjcamvis.auth.eu-north-1.amazoncognito.com";

    /**
     * An allowed callback URL.
     */
   //@Value("${spring.application.cognito.logout-redirect-uri}")
    private String logoutRedirectUrl;

    /**
     * The ID of your User Pool Client.
     */
    //@Value("${spring.security.oauth2.client.registration.cognito.client-id}")
    private String userPoolClientId;

    public CognitoLogoutHandler(String logoutRedirectUri, String clientId) {
        this.logoutRedirectUrl = logoutRedirectUri;
        this.userPoolClientId = clientId;
    }


    /**
     * Here, we must implement the new logout URL request. We define what URL to send our request to, and set out client_id and logout_uri parameters.
     */
    @Override
    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        return UriComponentsBuilder
                .fromUri(URI.create(domain + "/logout"))
                .queryParam("client_id", userPoolClientId)
                .queryParam("logout_uri", logoutRedirectUrl)
                .encode(StandardCharsets.UTF_8)
                .build()
                .toUriString();
    }
}