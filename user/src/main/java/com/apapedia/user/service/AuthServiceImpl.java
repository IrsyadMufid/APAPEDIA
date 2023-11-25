package com.apapedia.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.apapedia.user.dto.request.CreateUserRequestDTO;
import com.apapedia.user.model.Customer;
import com.apapedia.user.model.RoleEnum;
import com.apapedia.user.model.Seller;
import com.apapedia.user.model.UserModel;
import com.apapedia.user.repository.CustomerDb;
import com.apapedia.user.repository.SellerDb;
import com.apapedia.user.repository.UserDb;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    UserDb userDb;

    @Autowired
    SellerDb sellerDb;

    @Autowired
    CustomerDb customerDb;

    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public UserModel Register(CreateUserRequestDTO createUserDTO) {
        if (createUserDTO != null) {
            if (createUserDTO.getRole().equals("Seller")) {
                Seller seller = new Seller();
                seller.setName(createUserDTO.getName());
                seller.setUsername(createUserDTO.getUsername());
                seller.setPassword(passwordEncoder.encode(createUserDTO.getPassword()));
                seller.setEmail(createUserDTO.getEmail());
                seller.setAddress(createUserDTO.getAddress());
                seller.setRole(RoleEnum.Seller);
                seller.setCategory(createUserDTO.getCategory());
                sellerDb.save(seller);
                return seller;
            } else if (createUserDTO.getRole().equals("Customer")) {
                Customer customer = new Customer();
                customer.setName(createUserDTO.getName());
                customer.setUsername(createUserDTO.getUsername());
                customer.setPassword(passwordEncoder.encode(createUserDTO.getPassword()));
                customer.setEmail(createUserDTO.getEmail());
                customer.setAddress(createUserDTO.getAddress());
                customer.setRole(RoleEnum.Customer);
                customerDb.save(customer);
                return customer;
            }
        }
        return null;
    }
}
