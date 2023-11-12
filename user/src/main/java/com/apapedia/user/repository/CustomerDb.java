package com.apapedia.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.apapedia.user.model.Customer;
import java.util.UUID;

@Repository
public interface CustomerDb extends JpaRepository<Customer, UUID> {
    
}
