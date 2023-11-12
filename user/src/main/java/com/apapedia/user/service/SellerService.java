package com.apapedia.user.service;

import java.util.UUID;

import com.apapedia.user.model.Seller;

public interface SellerService {
    
    void createSeller(Seller seller);
    Seller getSellerById(UUID id);
    void updateBalanceWithdraw(UUID id, Long balanceInput);
}
