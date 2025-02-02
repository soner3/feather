package com.feather.authserver.service.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.authentication.password.CompromisedPasswordDecision;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.feather.authserver.dto.CreateUserDto;
import com.feather.authserver.exception.CompromisedPasswordException;
import com.feather.authserver.model.Authority;
import com.feather.authserver.model.FeatherRole;
import com.feather.authserver.model.Role;
import com.feather.authserver.model.User;
import com.feather.authserver.repository.RoleRepository;
import com.feather.authserver.repository.UserRepository;
import com.feather.authserver.service.UserService;
import com.feather.lib.exception.NotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CompromisedPasswordChecker passwordChecker;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Collection<? extends GrantedAuthority> loadAuthorities(User user) {
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        Role role = user.getRole();
        Set<Authority> authorities = role.getAuthorities();
        for (Authority authority : authorities) {
            grantedAuthorities.add(new SimpleGrantedAuthority(authority.getName()));
        }

        grantedAuthorities.add(new SimpleGrantedAuthority(role.getName().toString()));

        return grantedAuthorities;

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

    @Override
    public User createAdminUser(CreateUserDto createUserDto) {
        checkPassword(createUserDto.password());
        Role adminRole = getRole(FeatherRole.ROLE_ADMIN);
        return createUser(createUserDto, adminRole);

    }

    @Override
    public User createStaffUser(CreateUserDto createUserDto) {
        checkPassword(createUserDto.password());
        Role staffRole = getRole(FeatherRole.ROLE_STAFF);
        return createUser(createUserDto, staffRole);

    }

    @Override
    public User createUser(CreateUserDto createUserDto) {
        checkPassword(createUserDto.password());
        Role userRole = getRole(FeatherRole.ROLE_USER);
        return createUser(createUserDto, userRole);

    }

    private Role getRole(FeatherRole role) {
        return roleRepository.findByName(role)
                .orElseThrow(() -> new NotFoundException("Role not found for name: " + role.toString()));
    }

    private User createUser(CreateUserDto createUserDto, Role role) {
        User user = new User(createUserDto.email(), createUserDto.username(), createUserDto.phoneNumber(),
                createUserDto.firstName(), createUserDto.lastName(), passwordEncoder.encode(createUserDto.password()),
                role);
        return userRepository.save(user);
    }

    private void checkPassword(String password) {
        CompromisedPasswordDecision passwordDecision = passwordChecker.check(password);
        if (passwordDecision.isCompromised()) {
            throw new CompromisedPasswordException(
                    "The giving password:" + password + " is compromised, enter a stronger password");
        }
    }

}
