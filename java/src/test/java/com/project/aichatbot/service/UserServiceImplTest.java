package com.project.aichatbot.service;

import com.project.aichatbot.dto.UserDTO;
import com.project.aichatbot.entity.User;
import com.project.aichatbot.exception.UserAlreadyExistsException;
import com.project.aichatbot.repository.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private UserDTO userDTO;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        // Mock UserDTO
        userDTO = new UserDTO();
        userDTO.setEmail("test@example.com");
        userDTO.setFirstName("John");
        userDTO.setLastName("Doe");
        userDTO.setPassword("password123");

        // Mock passwordEncoder behavior
        Mockito.when(passwordEncoder.encode(Mockito.any(CharSequence.class))).thenReturn("encodedPassword");

    }

    @Test
    public void testGetAllUsers() {
        // Create a User object
        User user = new User();
        user.setEmail("test@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPassword("password123");

        // Setup mock behavior
        List<User> userList = new ArrayList<>();
        userList.add(user);
        Mockito.when(userRepository.findAll()).thenReturn(userList);

        // Perform the test
        List<User> result = userService.getAllUsers();

        // Assertions
        Assert.assertNotNull(result);
        Assert.assertEquals(1, result.size());
        Assert.assertEquals(user.getEmail(), result.get(0).getEmail());
    }

    @Test
    public void testRegisterUser() {
        // Setup mock behavior
        Mockito.when(userRepository.existsByEmail(userDTO.getEmail())).thenReturn(false);

        // Perform the test
        userService.registerUser(userDTO);

        // Assertions
        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any(User.class));
    }

    @Test(expected = UserAlreadyExistsException.class)
    public void testRegisterUserWithExistingEmail() {
        // Setup mock behavior
        Mockito.when(userRepository.existsByEmail(userDTO.getEmail())).thenReturn(true);
        // Perform the test
        userService.registerUser(userDTO);
    }

    @Test
    public void testFindByEmail() {
        // Create a user object for testing
        User user = new User();
        user.setEmail("test@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPassword("password");

        // Setup mock behavior
        Mockito.when(userRepository.findByEmail(userDTO.getEmail())).thenReturn(user);

        // Perform the test
        User result = userService.findByEmail(userDTO.getEmail());

        // Assertions
        Assert.assertNotNull(result);
        Assert.assertEquals(user.getEmail(), result.getEmail());
    }

}
