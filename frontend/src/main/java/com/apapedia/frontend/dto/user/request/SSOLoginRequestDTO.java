package com.apapedia.frontend.dto.user.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SSOLoginRequestDTO {
    private String username;
    private String name;
}