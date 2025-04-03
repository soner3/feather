package com.feather.authserver.service.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.authentication.password.CompromisedPasswordDecision;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.feather.authserver.dto.user.CreateUserDto;
import com.feather.authserver.dto.user.ResponseUserDto;
import com.feather.authserver.dto.user.UpdateUserDto;
import com.feather.authserver.exception.AlreadyExistsException;
import com.feather.authserver.exception.CompromisedPasswordException;
import com.feather.authserver.exception.NotFoundException;
import com.feather.authserver.model.FeatherRole;
import com.feather.authserver.model.Profile;
import com.feather.authserver.model.Role;
import com.feather.authserver.model.User;
import com.feather.authserver.repository.ProfileRepository;
import com.feather.authserver.repository.RoleRepository;
import com.feather.authserver.repository.UserRepository;
import com.feather.authserver.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ProfileRepository profileRepository;
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

        User user = new User(createUserDto.email(), createUserDto.username(),
                createUserDto.firstName(), createUserDto.lastName(), passwordEncoder.encode(createUserDto.password()),
                userRole);

        User savedUser = userRepository.save(user);
        profileRepository.save(new Profile(savedUser));

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

        if (!createUserDto.password().equals(createUserDto.rePassword())) {
            throw new CompromisedPasswordException(
                    "Password and Re-Password do not match");
        }

        if (userRepository.existsByEmail(createUserDto.email())) {
            throw new AlreadyExistsException("User with this email already exists");
        }

        if (userRepository.existsByUsername(createUserDto.username())) {
            throw new AlreadyExistsException("User with this username already exists");
        }

    }

    @Override
    @Transactional
    public void deleteUser(UUID userId) {
        int entityCount = userRepository.deleteByUserId(userId);
        if (entityCount <= 0) {
            throw new NotFoundException("No entity found to delete");
        }

    }

    @Override
    public ResponseEntity<ResponseUserDto> getUser(UUID userId) {
        User user = loadUser(userId);
        ResponseUserDto responseUserDto = conversionService.convert(user, ResponseUserDto.class);
        return ResponseEntity.ok(responseUserDto);

    }

    @Override
    @Transactional
    public ResponseEntity<ResponseUserDto> updateUser(UUID userId, UpdateUserDto updateUserDto) {
        User user = loadUser(userId);
        user.setEmail(updateUserDto.email());
        user.setUsername(updateUserDto.username());
        user.setFirstName(updateUserDto.firstName());
        user.setLastName(updateUserDto.lastName());

        User savedUser = userRepository.save(user);
        ResponseUserDto responseUserDto = conversionService.convert(savedUser, ResponseUserDto.class);
        return ResponseEntity.ok(responseUserDto);

    }

    public User loadUser(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("No user found for id: " + userId.toString()));
    }

}
