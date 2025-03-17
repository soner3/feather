package com.feather.authserver.data;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.feather.authserver.model.FeatherRole;
import com.feather.authserver.model.Role;
import com.feather.authserver.repository.RoleRepository;
import com.feather.authserver.service.UserService;
import com.feather.lib.dto.user.CreateUserDto;

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

                        userService.createUser(
                                        new CreateUserDto("admin@example.com", "admin", "+1234567890", "Admin", "User",
                                                        "AdminUser1234!"),
                                        FeatherRole.ROLE_ADMIN);

                        userService.createUser(
                                        new CreateUserDto("staff@example.com", "staff", "+1987654321", "Staff", "User",
                                                        "StaffUser1234!"),
                                        FeatherRole.ROLE_STAFF);

                        userService.createUser(
                                        new CreateUserDto("user@example.com", "user", "+1122334455", "Normal", "User",
                                                        "UserUser1234!"),
                                        FeatherRole.ROLE_USER);

                }

        }

}
