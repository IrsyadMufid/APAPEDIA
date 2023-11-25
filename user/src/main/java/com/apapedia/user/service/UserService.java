package com.apapedia.user.service;

import java.util.List;
import java.util.UUID;

import com.apapedia.user.model.UserModel;

public interface UserService {
    
    List<UserModel> findAllUser();

    UserModel findUserById(UUID id);

    void deleteUser(UserModel userToDelete);

    UserModel addBalanceUser(String id, Long balance);
    
    UserModel subtractBalanceUser(String id, Long balance);

    void editUser(UserModel user, String name, String username, String email, String password, String address);
}
