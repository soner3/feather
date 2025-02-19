package com.feather.authserver.service.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.authentication.password.CompromisedPasswordDecision;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.feather.authserver.model.FeatherRole;
import com.feather.authserver.model.Role;
import com.feather.authserver.model.User;
import com.feather.authserver.repository.RoleRepository;
import com.feather.authserver.repository.UserRepository;
import com.feather.authserver.service.UserService;
import com.feather.lib.dto.user.CreateUserDto;
import com.feather.lib.dto.user.ResponseUserDto;
import com.feather.lib.exception.AlreadyExistsException;
import com.feather.lib.exception.CompromisedPasswordException;
import com.feather.lib.exception.NotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CompromisedPasswordChecker passwordChecker;
    private final PasswordEncoder passwordEncoder;
    private final ConversionService conversionService;

    @Override
    public Collection<? extends GrantedAuthority> loadAuthorities(User user) {
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        Role role = user.getRole();

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

    public ResponseEntity<ResponseUserDto> createUser(CreateUserDto createUserDto, FeatherRole role) {
        validateCredentials(createUserDto);
        Role userRole;

        switch (role) {
            case ROLE_ADMIN:
                userRole = loadRoleFromUser(FeatherRole.ROLE_ADMIN);
                break;

            case ROLE_STAFF:
                userRole = loadRoleFromUser(FeatherRole.ROLE_STAFF);
                break;

            case ROLE_USER:
                userRole = loadRoleFromUser(FeatherRole.ROLE_USER);
                break;

            default:
                userRole = loadRoleFromUser(FeatherRole.ROLE_USER);
                break;
        }

        User user = new User(createUserDto.email(), createUserDto.username(), createUserDto.phoneNumber(),
                createUserDto.firstName(), createUserDto.lastName(), passwordEncoder.encode(createUserDto.password()),
                userRole);

        User savedUser = userRepository.save(user);
        ResponseUserDto responseUserDto = conversionService.convert(savedUser, ResponseUserDto.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseUserDto);

    }

    private Role loadRoleFromUser(FeatherRole role) {
        return roleRepository.findByName(role)
                .orElseThrow(() -> new NotFoundException("Role not found for name: " + role.toString()));
    }

    private void checkPassword(String password) {
        CompromisedPasswordDecision passwordDecision = passwordChecker.check(password);
        if (passwordDecision.isCompromised()) {
            throw new CompromisedPasswordException(
                    "The giving password:" + password + " is compromised, enter a stronger password");
        }
    }

    private void validateCredentials(CreateUserDto createUserDto) {
        checkPassword(createUserDto.password());
        if (userRepository.existsByEmail(createUserDto.email())) {
            throw new AlreadyExistsException("User with this email already exists");
        }

        if (userRepository.existsByUsername(createUserDto.username())) {
            throw new AlreadyExistsException("User with this username already exists");
        }

        if (userRepository.existsByPhoneNumber(createUserDto.phoneNumber())) {
            throw new AlreadyExistsException("User with this phonenumber already exists");
        }
    }

}
