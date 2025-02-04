package com.feather.lib.dto.user;

public record ResponseUserDto(
        String email,
        String username,
        String phoneNumber,
        String firstName,
        String lastName) {

}
