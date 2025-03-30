package com.feather.authserver.mapper;

import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;

import com.feather.authserver.dto.user.ResponseUserDto;
import com.feather.authserver.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper extends Converter<User, ResponseUserDto> {

    ResponseUserDto convert(User source);

}
