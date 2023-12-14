package com.apapedia.frontend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class JwtDTO {
    private String accessToken;
    private String tokenType = "Bearer ";

    public JwtDTO(String accessToken) {
        this.accessToken = accessToken;
    }
}