package com.feather.authserver.controller.impl;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.feather.authserver.controller.UserController;
import com.feather.authserver.dto.user.CreateUserDto;
import com.feather.authserver.dto.user.ResponseUserDto;
import com.feather.authserver.dto.user.UpdateUserDto;
import com.feather.authserver.exception.CompromisedPasswordException;
import com.feather.authserver.model.FeatherRole;
import com.feather.authserver.service.UserService;
import com.feather.authserver.util.HttpErrorInfo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/user")
@RequiredArgsConstructor
public class UserControllerImpl implements UserController {

    private final UserService userService;

    @Override
    @PostMapping("/public")
    @Operation(summary = "Create user", description = "Creates an user.", responses = {
            @ApiResponse(responseCode = "201", description = "User deleted", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseUserDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class)))
    })
    public ResponseEntity<ResponseUserDto> createUser(@RequestBody @Valid CreateUserDto createUserDto) {
        return userService.createUser(createUserDto, FeatherRole.ROLE_USER);
    }

    @Override
    @DeleteMapping("/{userId}")
    @Operation(summary = "Deletes a user", description = "Deletes an existing user.", responses = {
            @ApiResponse(responseCode = "204", description = "User deleted", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseUserDto.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class)))
    })
    public ResponseEntity<Void> deleteUser(@PathVariable(required = true, name = "userId") UUID userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @Override
    @GetMapping("/{userId}")
    @Operation(summary = "Retrieves a user", description = "Retrieves an existing user.", responses = {
            @ApiResponse(responseCode = "200", description = "User retrieved", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseUserDto.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class)))
    })
    public ResponseEntity<ResponseUserDto> getUser(@PathVariable(required = true, name = "userId") UUID userId) {
        return userService.getUser(userId);
    }

    @Override
    @PutMapping("/{userId}")
    @Operation(summary = "Update user details", description = "Updates an existing user.", responses = {
            @ApiResponse(responseCode = "200", description = "User updated", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseUserDto.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class)))
    })
    public ResponseEntity<ResponseUserDto> updateUser(@PathVariable(required = true, name = "userId") UUID userId,
            @RequestBody @Valid UpdateUserDto updateUserDto) {
        return userService.updateUser(userId, updateUserDto);
    }

    @ExceptionHandler
    public ResponseEntity<ProblemDetail> compromisedPasswordExceptionHandler(CompromisedPasswordException ex) {
        return HttpErrorInfo.errorInfo(ex, "Weak Password", HttpStatus.BAD_REQUEST);
    }

}
