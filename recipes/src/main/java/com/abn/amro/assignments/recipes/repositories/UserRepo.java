package com.abn.amro.assignments.recipes.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.abn.amro.assignments.recipes.entities.UserEntity;

public interface UserRepo extends JpaRepository<UserEntity, Long> {
	Optional<UserEntity> findByUsername(String username);
}
