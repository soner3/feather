package com.feather.authserver.mapper;

import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;

import com.feather.authserver.model.User;
import com.feather.lib.dto.user.ResponseUserDto;

@Mapper(componentModel = "spring")
public interface UserMapper extends Converter<User, ResponseUserDto> {

    ResponseUserDto convert(User source);

}
