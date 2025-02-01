package com.feather.authserver.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.feather.authserver.model.UserGroup;

public interface UserGroupRepository extends JpaRepository<UserGroup, UUID> {

}
