package com.apapedia.user.model;

import java.util.Date;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@SQLDelete(sql = "UPDATE user SET is_deleted = true WHERE id = ?")
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name="main_user")
public class UserModel {
    @Id
    private UUID id = UUID.randomUUID();

    @NotNull
    @Column(name="name", nullable=false)
    private String name;

    @NotNull
    @Column(name="username", unique = true, nullable=false)
    private String username;

    @Column(name="password", unique = true, nullable=false)
    private String password;

    @Column(name="email", unique = true, nullable=false)
    private String email;

    @Column(name="balance")
    private Long balance = 0L;

    @Column(name="address", nullable=false)
    private String address;

    @CreationTimestamp
    @Column(name="createdAt", nullable=false)
    private Date createdAt;

    @UpdateTimestamp
    @Column(name="updatedAt", nullable=false)
    private Date updatedAt;

    @Column(name="role", nullable=false)
    @Enumerated(EnumType.STRING)
    @JsonIgnore
    private RoleEnum role;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;
}
