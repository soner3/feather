package com.feather.authserver.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.feather.authserver.model.User;

public interface UserRepository extends JpaRepository<User, UUID> {

}
