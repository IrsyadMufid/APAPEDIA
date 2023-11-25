package com.apapedia.order.repository;

import jakarta.transaction.Transactional;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.apapedia.order.model.Cart;

@Repository
@Transactional
public interface CartDb extends JpaRepository<Cart, UUID> {
    Optional<Cart> findByUserId(UUID userId);
}
