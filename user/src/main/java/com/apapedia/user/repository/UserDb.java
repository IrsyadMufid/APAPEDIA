package com.apapedia.user.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.apapedia.user.model.UserModel;

@Repository
public interface UserDb extends JpaRepository <UserModel, UUID> {
    Optional<UserModel> findById(UUID id);

    UserModel findByUsername(String username);

    UserModel findByEmail(String email);
}
