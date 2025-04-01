package com.feather.authserver.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.feather.authserver.model.Profile;

public interface ProfileRepository extends JpaRepository<Profile, UUID> {

}
