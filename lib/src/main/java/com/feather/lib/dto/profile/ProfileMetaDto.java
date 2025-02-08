package com.feather.lib.dto.profile;

import java.time.LocalDateTime;
import java.util.UUID;

public record ProfileMetaDto(UUID id, boolean isOnline, LocalDateTime lastOnline) {

}
