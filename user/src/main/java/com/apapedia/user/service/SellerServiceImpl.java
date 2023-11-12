package com.apapedia.user.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apapedia.user.model.Seller;
import com.apapedia.user.repository.SellerDb;

@Service
public class SellerServiceImpl implements SellerService{

    @Autowired
    SellerDb sellerDb;

    @Override
    public void createSeller(Seller seller){
        sellerDb.save(seller);
    }

    @Override
    public Seller getSellerById(UUID id){
        return sellerDb.findById(id).get();
    }

    @Override
    public void updateBalanceWithdraw(UUID id, Long balanceInput){
        Seller seller = getSellerById(id);
        if (seller != null){
            seller.setBalance(seller.getBalance() - balanceInput);
            sellerDb.save(seller);
        }
    }

}