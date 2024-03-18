package com.project.aichatbot.service;

import com.project.aichatbot.entity.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@RunWith(MockitoJUnitRunner.class)
public class CustomUserDetailsServiceTest {

    @Mock
    private UserServiceImpl userService;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    private User user;

    @Before
    public void setUp() {
        // Mock User
        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPassword("password123");
    }

    @Test
    public void testLoadUserByUsername() {
        // Setup mock behavior
        Mockito.when(userService.findByEmail(user.getEmail())).thenReturn(user);
        // Perform the test
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(user.getEmail());
        // Assertions
        Assert.assertNotNull(userDetails);
        Assert.assertEquals(user.getEmail(), userDetails.getUsername());
    }

    @Test(expected = UsernameNotFoundException.class)
    public void testLoadUserByUsernameWithNonExistingUser() {
        // Setup mock behavior
        Mockito.when(userService.findByEmail(user.getEmail())).thenReturn(null);
        // Perform the test
        customUserDetailsService.loadUserByUsername(user.getEmail());
    }
}
