package com.project.aichatbot.controller;

import com.project.aichatbot.controller.UserController;
import com.project.aichatbot.dto.UserDTO;
import com.project.aichatbot.entity.User;
import com.project.aichatbot.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAllUsers() {
        // Prepare sample user list
        List<User> userList = Arrays.asList(
                new User("user1@example.com", "John", "Doe", "password1"),
                new User("user2@example.com", "Jane", "Doe", "password2")
        );

        // Mock UserService behavior
        when(userService.getAllUsers()).thenReturn(userList);

        // Call the getAllUsers method
        ResponseEntity<List<User>> responseEntity = userController.getAllUsers();

        // Verify the response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(userList, responseEntity.getBody());
    }

    @Test
    public void testRegisterUser() {
        // Prepare sample UserDTO
        UserDTO userDTO = new UserDTO("user@example.com", "John", "Doe", "password");

        // Call the registerUser method
        ResponseEntity<String> responseEntity = userController.registerUser(userDTO);

        // Verify that UserService's registerUser method is called with the provided UserDTO
        verify(userService, times(1)).registerUser(userDTO);

        // Verify the response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("User created successfully", responseEntity.getBody());
    }
}
