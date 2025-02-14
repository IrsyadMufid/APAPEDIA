package com.apapedia.frontend.dto.user.request;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdateUserRequestDTO extends CreateUserRequestDTO {
    private UUID id;

    private Long balance;

    private String role;
}
