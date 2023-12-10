package com.apapedia.user.dto.request;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
public class CreateUserRequestDTO {
    @NotBlank
    private String name;

    @NotBlank
    private String username;

    private String password;

    @NotBlank
    private String email;

    @NotBlank
    private String address;

    @NotBlank
    private String role;

    private UUID cartId;

    private String category;
}
