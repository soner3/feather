package com.feather.authserver.dto.profile;

import java.time.LocalDateTime;
import java.util.UUID;

import com.feather.authserver.model.User;

public record ResponseProfileDto(
        UUID profileId,
        char gender,
        boolean online,
        User user,
        String createdBy,
        String updatedBy,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {

}
