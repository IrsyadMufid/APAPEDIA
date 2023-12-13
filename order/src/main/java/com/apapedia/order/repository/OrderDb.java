package com.apapedia.order.repository;

import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.apapedia.order.model.Order;

@Repository
@Transactional
public interface OrderDb extends JpaRepository<Order, UUID> {
    List<Order> findByCustomer(UUID customer);
    List<Order> findBySeller(UUID seller);
}
