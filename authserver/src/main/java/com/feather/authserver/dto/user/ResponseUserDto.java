package com.feather.authserver.dto.user;

import java.time.LocalDateTime;
import java.util.UUID;

public record ResponseUserDto(
                UUID userId,
                String email,
                String username,
                String firstName,
                String lastName,
                String createdBy,
                String updatedBy,
                LocalDateTime createdAt,
                LocalDateTime updatedAt) {

}
