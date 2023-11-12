package com.apapedia.user.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.apapedia.user.model.User;

@Repository
public interface UserDb extends JpaRepository <User, UUID> {
    Optional<User> findById(UUID id);
}
