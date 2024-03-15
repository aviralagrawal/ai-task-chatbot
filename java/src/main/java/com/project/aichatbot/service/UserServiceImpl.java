package com.project.aichatbot.service;

import com.project.aichatbot.dto.UserDTO;
import com.project.aichatbot.entity.User;
import com.project.aichatbot.exception.UserAlreadyExistsException;
import com.project.aichatbot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;


    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void registerUser(UserDTO userDTO) {
        // Check if the email is already registered
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new UserAlreadyExistsException("User with email " + userDTO.getEmail() + " already exists.");
        }

        // Create a new user
        User user = new User();
        user.setEmail(userDTO.getEmail());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        userRepository.save(user);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
