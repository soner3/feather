package com.feather.authserver.service.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import com.feather.authserver.model.Group;
import com.feather.authserver.model.GroupAuthority;
import com.feather.authserver.model.User;
import com.feather.authserver.model.UserGroup;
import com.feather.authserver.repository.UserRepository;
import com.feather.authserver.service.UserService;
import com.feather.lib.exception.NotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public Collection<? extends GrantedAuthority> loadAuthorities(User user) {
        Set<String> authorityNames = new HashSet<>();
        List<UserGroup> userGroups = user.getUserGroups();
        for (UserGroup userGroup : userGroups) {
            Group group = userGroup.getGroup();
            List<GroupAuthority> groupAuthorities = group.getGroupAuthorities();
            for (GroupAuthority groupAuthority : groupAuthorities) {
                authorityNames.add(groupAuthority.getAuthority().getName());
            }
        }

        Set<GrantedAuthority> authorities = new HashSet<>();
        for (String authorityName : authorityNames) {
            authorities.add(new SimpleGrantedAuthority(authorityName));
        }

        return authorities;

    }

    @Override
    public User loadUserByUsernameOrEmail(String usernameOrEmail) {
        return userRepository
                .findByUsername(usernameOrEmail)
                .orElseGet(
                        () -> userRepository
                                .findByEmail(usernameOrEmail)
                                .orElseThrow(() -> new NotFoundException("No user found for: " + usernameOrEmail)));
    }

}
