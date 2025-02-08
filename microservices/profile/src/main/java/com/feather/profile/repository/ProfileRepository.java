package com.feather.profile.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.feather.profile.model.Profile;

public interface ProfileRepository extends JpaRepository<Profile, UUID> {

}
