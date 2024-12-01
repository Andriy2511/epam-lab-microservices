package org.example.finalprojectepamlabapplication.controller.implementation;

import jakarta.validation.Valid;
import org.example.finalprojectepamlabapplication.DTO.endpointDTO.ChangeLoginRequestDTO;
import org.example.finalprojectepamlabapplication.DTO.modelDTO.UserDTO;
import org.example.finalprojectepamlabapplication.controller.UserController;
import org.example.finalprojectepamlabapplication.exception.UnauthorizedException;
import org.example.finalprojectepamlabapplication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserControllerImpl implements UserController {

    private final UserService userService;

    @Autowired
    public UserControllerImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    @GetMapping("/{id}")
    public String getUserLogin(@PathVariable Long id){
        UserDTO userDTO = userService.getUserById(id);
        return userDTO.getUsername();
    }

    @Override
    @PutMapping("/{id}")
    public void changePassword(@PathVariable Long id, @ModelAttribute @Valid ChangeLoginRequestDTO changeLoginRequestDTO){
        UserDTO userDTO = userService.getUserById(id);
        if(userService.isOldPasswordSimilarToCurrentPassword(id, changeLoginRequestDTO.getOldPassword())) {
            userService.updateUserPassword(userDTO, changeLoginRequestDTO.getNewPassword());
        } else {
            throw new UnauthorizedException("The old password is incorrect");
        }
    }

    @Override
    @PatchMapping("/{id}")
    public void changeStatus(@PathVariable Long id){
        userService.changeActiveStatus(id);
    }
}
