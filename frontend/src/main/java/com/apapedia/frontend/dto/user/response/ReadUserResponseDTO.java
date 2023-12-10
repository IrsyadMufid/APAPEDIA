package com.apapedia.frontend.dto.user.response;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReadUserResponseDTO {
    private UUID id;
    private String name;
    private String username;
    private String email;
    private String address;
    private String role;
    private Long balance;
    private UUID cartId;
    private String category;
}
