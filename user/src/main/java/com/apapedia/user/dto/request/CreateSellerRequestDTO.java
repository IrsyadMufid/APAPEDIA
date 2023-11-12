package com.apapedia.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateSellerRequestDTO{
    private String name;
    private String username;
    private String password;
    private String email;
    private Long balance;
    private String address;
    private String category;
}