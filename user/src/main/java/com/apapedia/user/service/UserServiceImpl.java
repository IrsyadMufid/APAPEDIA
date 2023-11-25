package com.apapedia.user.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apapedia.user.model.UserModel;
import com.apapedia.user.repository.UserDb;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDb userDb;

    @Override
    public List<UserModel> findAllUser() {
        return userDb.findAll();
    }

    @Override
    public UserModel findUserById(UUID id) {
        for (UserModel user : userDb.findAll()) {
            if (user.getId().equals(id)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public void deleteUser(UserModel userToDelete) {
        userDb.delete(userToDelete);
    }

    @Override
    public UserModel addBalanceUser(String id, Long balance) {
        UserModel user = findUserById(UUID.fromString(id));
        user.setBalance(user.getBalance() + balance);
        userDb.save(user);
        return user;
    }

    @Override
    public UserModel subtractBalanceUser(String id, Long balance) {
        UserModel user = findUserById(UUID.fromString(id));
        user.setBalance(user.getBalance() - balance);
        userDb.save(user);
        return user;
    }

    @Override
    public void editUser(UserModel user, String name, String username, String email, String password, String address) {
        user.setName(name);
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setAddress(address);
        userDb.save(user);
    }
}
