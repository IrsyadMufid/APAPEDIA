package com.apapedia.user.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class JwtResponseDTO {
    private String accessToken;
    private String tokenType = "Bearer ";

    public JwtResponseDTO(String accessToken) {
        this.accessToken = accessToken;
    }
}
