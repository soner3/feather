package com.feather.profile.client;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

import com.feather.lib.dto.user.CreateUserDto;
import com.feather.lib.dto.user.ResponseUserDto;

import jakarta.validation.Valid;

@HttpExchange
public interface UserClientService {

    @PostExchange("/v1/user/public")
    ResponseEntity<ResponseUserDto> createUser(@RequestBody @Valid CreateUserDto createUserDto);

}
