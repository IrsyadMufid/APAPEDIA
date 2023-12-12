package com.apapedia.frontend.dto.user;

import org.mapstruct.Mapper;

import com.apapedia.frontend.dto.user.response.ReadUserResponseDTO;

@Mapper(componentModel = "spring")
public interface UserMapper {
    ReadUserResponseDTO userToReadUserDTO(ReadUserResponseDTO user);
}
