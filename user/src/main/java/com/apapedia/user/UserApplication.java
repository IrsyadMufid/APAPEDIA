package com.apapedia.user;

import java.util.Locale;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.apapedia.user.dto.SellerMapper;
import com.apapedia.user.dto.request.CreateSellerRequestDTO;
import com.apapedia.user.service.SellerService;
import com.github.javafaker.Faker;

import jakarta.transaction.Transactional;

@SpringBootApplication
public class UserApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserApplication.class, args);
	}

	@Bean
	@Transactional
	CommandLineRunner run(SellerService sellerService, SellerMapper sellerMapper){
		return args -> {
			var faker = new Faker(new Locale("in-ID"));

			// Membuat fake data User Seller (blm sesuai ketentuan)
			var nama = faker.name().firstName();
			var username = faker.name().lastName();
			var password = faker.name().fullName();
			var email = faker.name().firstName() + "@gmail.com";
			Long balance = Long.valueOf(100000);
			var address = faker.address().cityName();
			var category = "Biasa";

			var sellerDTO = new CreateSellerRequestDTO();
			sellerDTO.setName(nama);
			sellerDTO.setUsername(username);
			sellerDTO.setPassword(password);
			sellerDTO.setEmail(email);
			sellerDTO.setBalance(balance);
			sellerDTO.setAddress(address);
			sellerDTO.setCategory(category);

			var seller = sellerMapper.createSellerRequestDTOToSeller(sellerDTO);
			sellerService.createSeller(seller);

		};
	}

}
