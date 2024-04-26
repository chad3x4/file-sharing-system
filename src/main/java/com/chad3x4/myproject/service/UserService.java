package com.chad3x4.myproject.service;

import java.util.List;

import com.chad3x4.myproject.dto.UserDTO;
import com.chad3x4.myproject.model.User;

public interface UserService {
    public List<UserDTO> findAll();
    public UserDTO findUser(String username);
    public UserDTO addUser(User newUser);
    public UserDTO saveUser(UserDTO updatedUser, String currentUsername);
    public void deleteUserByUsername(String username);
}
