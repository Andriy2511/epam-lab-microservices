package org.example.finalprojectepamlabapplication.service;

import org.example.finalprojectepamlabapplication.DTO.modelDTO.UserDTO;
import org.example.finalprojectepamlabapplication.model.User;

import java.util.List;

public interface UserService {

    List<User> getAllUsers();

    UserDTO getUserById(Long id);

    UserDTO getUserByUsername(String username);

    UserDTO updateUser(UserDTO userDTO);

    UserDTO updateUserPassword(UserDTO userDTO, String password);

    UserDTO changeActiveStatus(Long id);

    User setUsernameAndPasswordForUser(User user);

    boolean isOldPasswordSimilarToCurrentPassword(Long id, String previousPassword);
}
