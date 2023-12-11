package com.apapedia.user.service;

import java.util.List;
import java.util.UUID;

import com.apapedia.user.dto.request.UpdateUserRequestDTO;
import com.apapedia.user.model.Customer;
import com.apapedia.user.model.Seller;
import com.apapedia.user.model.UserModel;

public interface UserService {
    
    List<UserModel> findAllUser();

    UserModel findUserById(UUID id);

    Customer findCustomerById(UUID id);

    Seller findSellerById(UUID id);

    void deleteUser(UserModel userToDelete);

    UserModel addBalanceUser(String id, Long balance);
    
    UserModel subtractBalanceUser(String id, Long balance);

    void editUser(UserModel user, UpdateUserRequestDTO updateUserRequestDTO);

    void editUserSeller(Seller seller, UpdateUserRequestDTO updateUserRequestDTO);

    void editUserCustomer(Customer customer, UpdateUserRequestDTO updateUserRequestDTO);
}
