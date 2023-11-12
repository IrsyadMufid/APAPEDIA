package com.apapedia.user.controller;

import java.util.NoSuchElementException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import com.apapedia.user.dto.SellerMapper;
import com.apapedia.user.dto.request.UpdateSellerRequestDTO;
import com.apapedia.user.service.SellerService;
import org.springframework.http.HttpStatus;

import org.springframework.ui.Model;

@Controller
public class SellerController {
    
    @Autowired
    private SellerMapper sellerMapper;

    @Autowired
    private SellerService sellerService;

    @GetMapping(value = "/seller/{id}")
    private Buku retrieveBuku(@PathVariable("id") UUID id) {
        try {
            return bukuRestService.getSellerById(UUID.fromString(id));
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Id Seller" + id + " not found"
            );
        }
    }
    @GetMapping("seller/{id}/profile/withdraw")
    public String formUpdateBuku(@PathVariable("id") UUID id, Model model){
        var seller = sellerService.getSellerById(id);
        var sellerDTO = sellerMapper.sellerToUpdateSellerRequestDTO(seller);
        model.addAttribute("sellerDTO", sellerDTO);
        return "seller-withdraw";
    }
    
    @PostMapping("seller/profile/withdraw")
    public String withdrawBalance(@ModelAttribute UpdateSellerRequestDTO updateSellerRequestDTO, Model model){
        var seller = sellerService.getSellerById(updateSellerRequestDTO.getId());
        Long balance = seller.getBalance();
        if (balance >= updateSellerRequestDTO.getBalance()){
            sellerService.updateBalanceWithdraw(seller.getId(), updateSellerRequestDTO.getBalance());
            var sellerBaru = sellerService.getSellerById(seller.getId());
            var sellerDTO = sellerMapper.sellerToUpdateSellerRequestDTO(sellerBaru);
            model.addAttribute("sellerDTO", sellerDTO);
            return "seller-withdraw-success";
        } else{
            var sellerDTO = sellerMapper.sellerToUpdateSellerRequestDTO(seller);
            model.addAttribute("sellerDTO", sellerDTO);
            return "seller-withdraw-fail";
        }
    }
}
