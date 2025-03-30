package com.feather.authserver.config.user;

import java.util.Map;

import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.stereotype.Service;

import com.feather.authserver.model.User;
import com.feather.authserver.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OidcUserInfoService {

    private final UserService userService;

    public OidcUserInfo loadUser(String username) {
        User user = userService.loadUserByUsernameOrEmail(username);
        return new OidcUserInfo(createUserInfo(user));
    }

    private Map<String, Object> createUserInfo(User user) {
        return OidcUserInfo.builder()
                .subject(user.getUserId().toString())
                .name(user.getFirstName() + " " + user.getLastName())
                .givenName(user.getFirstName())
                .familyName(user.getLastName())
                .preferredUsername(user.getUsername())
                .email(user.getEmail())
                .build()
                .getClaims();

    }

}
