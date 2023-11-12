package com.apapedia.user.dto.request;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateSellerRequestDTO extends CreateSellerRequestDTO{
    private UUID id;
    private Long balance;
}
