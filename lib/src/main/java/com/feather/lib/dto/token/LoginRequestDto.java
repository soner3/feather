package com.feather.lib.dto.token;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequestDto(
        @NotBlank(message = "Username is required") @Size(min = 1, max = 32, message = "Username must be between 1 and 32 characters") String username,
        @NotBlank(message = "Password is required") @Size(min = 8, message = "Password must be at least 8 characters long") String password) {

}
