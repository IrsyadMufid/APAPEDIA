package com.apapedia.user.dto.request;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
public class UpdateUserRequestDTO extends CreateUserRequestDTO {
    private UUID id;

    private Long balance;

    private String role;
}
