package com.feather.authserver.config.user;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.feather.authserver.model.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsImpl implements UserDetails {

    private String userId;

    private String email;

    private String username;

    private String phoneNumber;

    private String firstName;

    private String lastName;

    private String password;

    private boolean isAccountNonExpired;

    private boolean isAccountNonLocked;

    private boolean isCredentialsNonExpired;

    private boolean isEnabled;

    private Collection<? extends GrantedAuthority> authorities;

    public static UserDetailsImpl build(User user, Collection<? extends GrantedAuthority> authorities) {
        return new UserDetailsImpl(
                user.getUserId().toString(),
                user.getEmail(),
                user.getUsername(),
                user.getPhoneNumber(),
                user.getFirstName(),
                user.getLastName(),
                user.getPassword(),
                user.isAccountNonExpired(),
                user.isAccountNonLocked(),
                user.isCredentialsNonExpired(),
                user.isEnabled(),
                authorities);

    }

}
