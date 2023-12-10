package com.apapedia.frontend.dto.user.request;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRequestDTO extends CreateUserRequestDTO {
    private UUID id;
}
