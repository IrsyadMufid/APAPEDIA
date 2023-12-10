package com.apapedia.user.dto.response;

import java.util.UUID;

import com.apapedia.user.dto.request.CreateUserRequestDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserResponseDTO extends CreateUserRequestDTO {
    private UUID id;

    private Long balance;

    private String role;
}
