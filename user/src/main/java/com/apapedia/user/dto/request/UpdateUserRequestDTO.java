package com.apapedia.user.dto.request;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateUserRequestDTO extends CreateUserRequestDTO {
    @NotBlank
    private String id;

    private UUID cartId;

    private String category;
}