package com.feather.authserver.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.feather.authserver.model.Group;

public interface GroupRepository extends JpaRepository<Group, UUID> {

}
