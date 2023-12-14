package com.apapedia.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.apapedia.user.dto.request.UpdateUserRequestDTO;
import com.apapedia.user.model.Customer;
import com.apapedia.user.model.RoleEnum;
import com.apapedia.user.model.Seller;
import com.apapedia.user.model.UserModel;
import com.apapedia.user.repository.CustomerDb;
import com.apapedia.user.repository.SellerDb;
import com.apapedia.user.repository.UserDb;
import com.apapedia.user.service.AuthServiceImpl;
import com.apapedia.user.service.UserServiceImpl;

@SpringBootTest
class UserApplicationTests {

	@Mock 
	private UserDb userDb;

	@Mock 
	private SellerDb sellerDb;

	@Mock 
	private CustomerDb customerDb;

	@InjectMocks
	private AuthServiceImpl authService;

	@InjectMocks
	private UserServiceImpl userService;

	@Mock
    private BCryptPasswordEncoder passwordEncoder;

	@BeforeEach
	public void setUp(){
		List<UserModel> userModels = new ArrayList<>();

		for (int i = 0 ; i<5; i++){
			var user1 = new Seller();
			user1.setName("JimmiBaru");
			user1.setUsername("Jimmi" + i);
			user1.setEmail("Jimmi" + i+ "@gmail.com");
			user1.setPassword(passwordEncoder.encode("secret10" + 1));
			user1.setAddress("Gedung Lama");
			user1.setRole(RoleEnum.SELLER);
			user1.setCategory("Official Store");
			var user2 = new Customer();
			user2.setName("AngelBaru");
			user2.setUsername("Angel" + i);
			user2.setEmail("Angel" + i +"@gmail.com");
			user2.setPassword(passwordEncoder.encode("secret11" + i));
			user2.setAddress("Gedung Baru");
			user2.setRole(RoleEnum.CUSTOMER);
			user2.setCartId(UUID.randomUUID());
			userModels.add(user1);
			userModels.add(user2);
			Mockito.when(userDb.findAll()).thenReturn(userModels);
		}
	}

	@Test
    public void testFindAllUser() {
        var listUser = userService.findAllUser();
		Mockito.when(userDb.findAll()).thenReturn(listUser);
        assertEquals(10, listUser.size());
    }

	@Test
	public void testDeleteUser(){
		var listUser = userService.findAllUser();
		assertEquals(10, userService.findAllUser().size());
		userService.deleteUser(userService.findAllUser().get(0));
		listUser.remove(0);
		Mockito.when(userService.findAllUser()).thenReturn(listUser);
		assertEquals(9, userService.findAllUser().size());
	}

	@Test
	void testFindByUserId() {
		var listUser = userService.findAllUser();
		var user = listUser.get(0);
		var userId = user.getId();
		Mockito.when(userDb.findById(userId)).thenReturn(Optional.of(user));
		UserModel result = userService.findUserById(userId);
		assertEquals(userId, result.getId());
	}

	@Test
	void testEditUser() {
		String name = "Chika";
		String address = "Jalan Mangga";
		UserModel existingUser = userService.findAllUser().get(0);
		existingUser.setName(name);
		existingUser.setAddress(address);

		var updateUserRequestDTO = new UpdateUserRequestDTO();
		updateUserRequestDTO.setName(name);
		updateUserRequestDTO.setAddress(address);
		Mockito.when(userDb.findById(existingUser.getId())).thenReturn(Optional.of(existingUser));
		Mockito.when(userDb.save(any(UserModel.class))).thenAnswer(i -> i.getArguments()[0]);
		userService.editUser(existingUser, updateUserRequestDTO);

		assertEquals("Chika", existingUser.getName());
		assertEquals("Jalan Mangga", existingUser.getAddress());

	}

	@Test
    void testTopUpBalanceUser() {
        var listUser = userService.findAllUser();
		UserModel customerUser = null;
		for (UserModel user : listUser) {
			if (user.getRole().equals(RoleEnum.CUSTOMER)) {
				customerUser = user;
				break;
			}
		}
        customerUser.setBalance(100L);
        UserModel updatedUser = userService.addBalanceUser(customerUser.getId().toString(), 50L);
        assertEquals(150L, updatedUser.getBalance());
    }

	@Test
	void testWithdrawBalanceUser() {
		var listUser = userService.findAllUser();
		UserModel sellerUser = null;
		for (UserModel user : listUser) {
			if (user.getRole().equals(RoleEnum.SELLER)) {
				sellerUser = user;
				break;
			}
		}
		sellerUser.setBalance(100L);
		UserModel updatedUser = userService.subtractBalanceUser(sellerUser.getId().toString(), 50L);
		assertEquals(50L, updatedUser.getBalance());
	}


}

