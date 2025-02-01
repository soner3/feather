package com.feather.authserver.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.feather.authserver.model.GroupAuthority;

public interface GroupAuthorityRepository extends JpaRepository<GroupAuthority, UUID> {

}
