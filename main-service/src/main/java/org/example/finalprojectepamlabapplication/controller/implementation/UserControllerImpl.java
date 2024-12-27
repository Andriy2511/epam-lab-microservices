package org.example.finalprojectepamlabapplication.controller.implementation;

import jakarta.validation.Valid;
import org.example.finalprojectepamlabapplication.DTO.endpointDTO.ChangeLoginRequestDTO;
import org.example.finalprojectepamlabapplication.DTO.modelDTO.UserDTO;
import org.example.finalprojectepamlabapplication.controller.UserController;
import org.example.finalprojectepamlabapplication.exception.UnauthorizedException;
import org.example.finalprojectepamlabapplication.security.GumUserDetails;
import org.example.finalprojectepamlabapplication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    @GetMapping
    public String getUserLogin(@AuthenticationPrincipal GumUserDetails userDetails){
        UserDTO userDTO = userService.getUserByUsername(userDetails.getUsername());
        return userDTO.getUsername();
    }

    @Override
    @PutMapping
    public void changePassword(@AuthenticationPrincipal GumUserDetails userDetails, @ModelAttribute @Valid ChangeLoginRequestDTO changeLoginRequestDTO){
        UserDTO userDTO = userService.getUserByUsername(userDetails.getUsername());
        if(userService.isOldPasswordSimilarToCurrentPassword(userDTO.getId(), changeLoginRequestDTO.getOldPassword())) {
            userService.updateUserPassword(userDTO, changeLoginRequestDTO.getNewPassword());
        } else {
            throw new UnauthorizedException("The old password is incorrect");
        }
    }

    @Override
    @PatchMapping
    public void changeStatus(@AuthenticationPrincipal GumUserDetails userDetails){
        userService.changeActiveStatus(userService.getUserByUsername(userDetails.getUsername()).getId());
    }
}
