package com.feather.lib.dto.user;

import java.util.UUID;

public record ResponseUserDto(
                UUID id,
                String email,
                String username,
                String phoneNumber,
                String firstName,
                String lastName) {

}
