package com.feather.profile.service.impl;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.feather.lib.dto.user.CreateUserDto;
import com.feather.lib.dto.user.ResponseUserDto;
import com.feather.profile.client.UserClientService;
import com.feather.profile.model.Profile;
import com.feather.profile.model.ProfileMeta;
import com.feather.profile.repository.ProfileMetaRepository;
import com.feather.profile.repository.ProfileRepository;
import com.feather.profile.service.ProfileService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Service
public class ProfileServiceImpl implements ProfileService {

    private final UserClientService userClientService;
    private final ProfileRepository profileRepository;
    private final ProfileMetaRepository profileMetaRepository;

    @Override
    public ResponseEntity<ResponseUserDto> registerUser(CreateUserDto createUserDto) {
        ResponseEntity<ResponseUserDto> responseEntity = userClientService.createUser(createUserDto);
        ResponseUserDto responseUserDto = responseEntity.getBody();
        Profile profile = profileRepository.save(new Profile(responseUserDto.id()));
        profileMetaRepository.save(new ProfileMeta(profile));
        return responseEntity;

    }

}
