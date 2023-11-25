package com.apapedia.user.dto;

import org.mapstruct.Mapper;

import com.apapedia.user.dto.request.CreateUserRequestDTO;
import com.apapedia.user.model.UserModel;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserModel createUserRequestDTOToUser(CreateUserRequestDTO createUserRequestDTO);
}
