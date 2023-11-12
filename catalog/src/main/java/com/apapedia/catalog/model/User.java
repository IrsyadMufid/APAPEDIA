package com.apapedia.catalog.model;

import java.time.Instant;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="user")
public class User {
    @Id
    private UUID id = UUID.randomUUID();

    @Column(name="name", nullable=false)
    private String name;

    @Column(name="username", nullable=false)
    private String username;

    @Column(name="password", nullable=false)
    private String password;

    @Column(name="email", nullable=false)
    private String email;

    @Column(name="balance", nullable=false)
    private Long balance;

    @Column(name="address", nullable=false)
    private String address;

    @CreationTimestamp
    @Column(name="createdAt", nullable=false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name="updatedAt", nullable=false)
    private Instant updatedAt;
}
