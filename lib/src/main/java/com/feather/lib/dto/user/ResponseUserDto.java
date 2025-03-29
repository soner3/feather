package com.feather.lib.dto.user;

import java.util.UUID;

public record ResponseUserDto(
        UUID userId,
        String email,
        String username,
        String phoneNumber,
        String firstName,
        String lastName) {

}
