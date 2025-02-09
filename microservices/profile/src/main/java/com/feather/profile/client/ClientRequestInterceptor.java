package com.feather.profile.client;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Component
public class ClientRequestInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
            throws IOException {
        String token = getAccessToken();
        request.getHeaders().set(HttpHeaders.AUTHORIZATION, token);
        return execution.execute(request, body);
    }

    private String getAccessToken() {
        log.info("Extracting JWT from request header of currently logged in user");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken) {
            Jwt jwt = (Jwt) ((JwtAuthenticationToken) authentication).getPrincipal();
            String token = jwt.getTokenValue();
            log.info("Successfully extracted JWT: {}", token);
            return token;
        }
        log.warn("No user is currently logged in");
        return "";
    }

}
