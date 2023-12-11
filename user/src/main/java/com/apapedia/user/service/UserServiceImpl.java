package com.apapedia.user.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.apapedia.user.dto.request.UpdateUserRequestDTO;
import com.apapedia.user.model.Customer;
import com.apapedia.user.model.Seller;
import com.apapedia.user.model.UserModel;
import com.apapedia.user.repository.CustomerDb;
import com.apapedia.user.repository.SellerDb;
import com.apapedia.user.repository.UserDb;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDb userDb;

    @Autowired
    private CustomerDb customerDb;

    @Autowired
    private SellerDb sellerDb;

    @Autowired
    PasswordEncoder passwordEncoder;

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
    public Customer findCustomerById(UUID id) {
        for (Customer customer : customerDb.findAll()) {
            if (customer.getId().equals(id)) {
                return customer;
            }
        }
        return null;
    }

    @Override
    public Seller findSellerById(UUID id) {
        for (Seller seller : sellerDb.findAll()) {
            if (seller.getId().equals(id)) {
                return seller;
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
    public void editUser(UserModel user, UpdateUserRequestDTO updateUserRequestDTO) {
        user.setUsername(updateUserRequestDTO.getUsername());
        user.setName(updateUserRequestDTO.getName());
        user.setPassword(passwordEncoder.encode(updateUserRequestDTO.getPassword()));
        user.setEmail(updateUserRequestDTO.getEmail());
        user.setAddress(updateUserRequestDTO.getAddress());
        userDb.save(user);
    }

    @Override
    public void editUserSeller(Seller seller, UpdateUserRequestDTO updateUserRequestDTO) {
        seller.setUsername(updateUserRequestDTO.getUsername());
        seller.setName(updateUserRequestDTO.getName());
        seller.setEmail(updateUserRequestDTO.getEmail());
        seller.setAddress(updateUserRequestDTO.getAddress());
        seller.setCategory(updateUserRequestDTO.getCategory());
        sellerDb.save(seller);
    }

    @Override
    public void editUserCustomer(Customer customer, UpdateUserRequestDTO updateUserRequestDTO) {
        customer.setUsername(updateUserRequestDTO.getUsername());
        customer.setName(updateUserRequestDTO.getName());
        customer.setPassword(passwordEncoder.encode(updateUserRequestDTO.getPassword()));
        customer.setEmail(updateUserRequestDTO.getEmail());
        customer.setAddress(updateUserRequestDTO.getAddress());
        customerDb.save(customer);
    }
}
