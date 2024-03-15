package com.project.aichatbot.service;

import com.project.aichatbot.dto.UserDTO;
import com.project.aichatbot.entity.User;

import java.util.List;

public interface UserService {

    List<User> getAllUsers();

    void registerUser(UserDTO userDTO);

    User findByEmail(String email);
}

