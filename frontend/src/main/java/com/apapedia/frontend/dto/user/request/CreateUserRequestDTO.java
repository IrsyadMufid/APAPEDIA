package com.apapedia.frontend.dto.user.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateUserRequestDTO {
    private String name;
    private String username;
    private String email;
    private String password;
    private String address;
    private String role;
    private String category;
}
