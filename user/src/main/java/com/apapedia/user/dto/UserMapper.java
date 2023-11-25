package com.apapedia.user.dto;

import org.mapstruct.Mapper;

import com.apapedia.user.dto.request.CreateUserRequestDTO;
import com.apapedia.user.dto.request.UpdateUserRequestDTO;
import com.apapedia.user.model.Customer;
import com.apapedia.user.model.Seller;
import com.apapedia.user.model.UserModel;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserModel createUserRequestDTOToUser(CreateUserRequestDTO createUserRequestDTO);
    Seller updateSellerRequestDTOToSeller(UpdateUserRequestDTO updateSellerRequestDTO);
    Customer updateCustomerRequestDTOToCustomer(UpdateUserRequestDTO updateCustomerRequestDTO);
    UpdateUserRequestDTO customerToUpdateCustomerRequestDTO(Customer customer);
    UpdateUserRequestDTO sellerToUpdateSellerRequestDTO(Seller seller);
}
