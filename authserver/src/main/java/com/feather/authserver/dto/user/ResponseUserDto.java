package com.feather.authserver.dto.user;

import java.util.UUID;

public record ResponseUserDto(
        UUID userId,
        String email,
        String username,
        String firstName,
        String lastName) {

}
