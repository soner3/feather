package com.feather.authserver.data;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.feather.authserver.dto.CreateUserDto;
import com.feather.authserver.model.FeatherRole;
import com.feather.authserver.model.Role;
import com.feather.authserver.repository.RoleRepository;
import com.feather.authserver.service.UserService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class InitilaizeData implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserService userService;

    @Override
    public void run(String... args) throws Exception {
        if (!roleRepository.existsByName(FeatherRole.ROLE_ADMIN)) {
            roleRepository.save(new Role(FeatherRole.ROLE_ADMIN));
            roleRepository.save(new Role(FeatherRole.ROLE_STAFF));
            roleRepository.save(new Role(FeatherRole.ROLE_USER));

            userService.createAdminUser(
                    new CreateUserDto("admin@example.com", "admin", "+1234567890", "Admin", "User", "Adm!n$@1234"));

            userService.createStaffUser(
                    new CreateUserDto("staff@example.com", "staff", "+1987654321", "Staff", "User", "St@ff!4321"));

            userService.createUser(
                    new CreateUserDto("user@example.com", "user", "+1122334455", "Normal", "User", "Us3rst!r0ng!"));

        }

    }

}
