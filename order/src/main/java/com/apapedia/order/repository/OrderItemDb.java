package com.apapedia.order.repository;

import jakarta.transaction.Transactional;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.apapedia.order.model.OrderItem;

@Repository
@Transactional
public interface OrderItemDb extends JpaRepository<OrderItem, UUID> {
    
}
