package com.feather.authserver.controller.impl;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.feather.authserver.controller.UserController;
import com.feather.authserver.exception.CompromisedPasswordException;
import com.feather.authserver.model.FeatherRole;
import com.feather.authserver.service.UserService;
import com.feather.lib.dto.user.CreateUserDto;
import com.feather.lib.dto.user.ResponseUserDto;
import com.feather.lib.util.HttpErrorInfo;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/user")
@RequiredArgsConstructor
public class UserControllerImpl implements UserController {

    private final UserService userService;

    @Override
    @PostMapping("/public")
    public ResponseEntity<ResponseUserDto> createUser(@Valid CreateUserDto createUserDto) {
        return userService.createUser(createUserDto, FeatherRole.ROLE_USER);
    }

    @ExceptionHandler
    public ResponseEntity<ProblemDetail> compromisedPasswordExceptionHandler(CompromisedPasswordException ex) {
        return HttpErrorInfo.errorInfo(ex, "Weak Password", HttpStatus.BAD_REQUEST);
    }

}
