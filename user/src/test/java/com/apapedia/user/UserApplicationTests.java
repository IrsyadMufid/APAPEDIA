package com.apapedia.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.apapedia.user.dto.request.CreateUserRequestDTO;
import com.apapedia.user.model.Customer;
import com.apapedia.user.model.RoleEnum;
import com.apapedia.user.model.Seller;
import com.apapedia.user.model.UserModel;
import com.apapedia.user.repository.CustomerDb;
import com.apapedia.user.repository.UserDb;
import com.apapedia.user.service.AuthService;
import com.apapedia.user.service.AuthServiceImpl;
import com.apapedia.user.service.UserService;
import com.apapedia.user.service.UserServiceImpl;

@SpringBootTest
class UserApplicationTests {

    @Mock
    private UserDb userDb;

	@Mock
    private CustomerDb customerDb;

	@Mock
	private UserService userService;

	@Mock
	private AuthService authService;

	@Mock
	private BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

	@InjectMocks
	private AuthServiceImpl authServiceImpl;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        List<UserModel> users = new ArrayList<>();

        var user1 = new Customer(null);
        user1.setName("Fathan");
        user1.setUsername("fathan200");
        user1.setPassword("fathan200");
        user1.setEmail("fathan@gmail.com");
        user1.setBalance(0L);
        user1.setAddress("Dirumah");
        user1.setCreatedAt(new Date());
        user1.setUpdatedAt(new Date());
        user1.setRole(RoleEnum.CUSTOMER);
        user1.setDeleted(false);

        var user2 = new Seller("Official-Store");
        user2.setName("Abin");
        user2.setUsername("abin200");
        user2.setPassword("abin200");
        user2.setEmail("abin@gmail.com");
        user2.setBalance(0L);
        user2.setAddress("Okee");
        user2.setCreatedAt(new Date());
        user2.setUpdatedAt(new Date());
        user2.setRole(RoleEnum.SELLER);
        user2.setDeleted(false);

        users.add(user1);
        users.add(user2);

        when(userDb.findAll()).thenReturn(users);
    }

    @Test
    void findAllUsersTest() {
        List<UserModel> retrievedUsers = userServiceImpl.findAllUser();
        assertEquals(2, retrievedUsers.size(), "Should return two users");
    }

	@Test
	void registerTest() {
		var user = new CreateUserRequestDTO();
		user.setName("oke");
		user.setUsername("oke");
		user.setPassword("oke213");
		user.setEmail("oke@gmail.com");
		user.setAddress("ehehe");
		user.setRole("customer");

		String result = authServiceImpl.register(user);
		assertEquals("Customer created", result, "Should return Customer created");
	}
}
