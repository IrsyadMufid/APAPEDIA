package com.apapedia.user.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class JwtResponseDTO {
    private String accessToken;

    public JwtResponseDTO(String accessToken) {
        this.accessToken = accessToken;
    }
}
