package com.feather.lib.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdateUserDto(
        @Email(message = "Invalid email format") @NotBlank(message = "Email is required") String email,

        @NotBlank(message = "Username is required") @Size(min = 1, max = 32, message = "Username must be between 1 and 32 characters") String username,

        @NotBlank(message = "Phone number is required") @Pattern(regexp = "\\+?[0-9]+", message = "Invalid phone number format") String phoneNumber,

        @NotBlank(message = "First name is required") String firstName,

        @NotBlank(message = "Last name is required") String lastName

) {

}
