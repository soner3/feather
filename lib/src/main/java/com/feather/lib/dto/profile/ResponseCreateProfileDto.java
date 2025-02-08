package com.feather.lib.dto.profile;

import java.util.UUID;

import com.feather.lib.dto.user.ResponseUserDto;

public record ResponseCreateProfileDto(
                UUID profileId,
                ResponseUserDto user) {

}
