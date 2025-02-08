package com.feather.profile.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.feather.profile.model.ProfileMeta;

public interface ProfileMetaRepository extends JpaRepository<ProfileMeta, UUID> {

}
