package com.feather.authserver.config;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {

    private String userId;

    private String email;

    private String username;

    private String phoneNumber;

    private String firstName;

    private String lastName;

    private String password;

    private Collection<? extends GrantedAuthority> authorities;

}
