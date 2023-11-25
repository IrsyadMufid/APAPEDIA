package com.apapedia.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.apapedia.user.model.Seller;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SellerDb extends JpaRepository<Seller, UUID> {
    Optional<Seller> findById(UUID id);
}
