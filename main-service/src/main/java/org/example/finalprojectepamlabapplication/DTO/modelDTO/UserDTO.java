package org.example.finalprojectepamlabapplication.DTO.modelDTO;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.example.finalprojectepamlabapplication.model.User;

@Getter
@ToString
@Builder(toBuilder = true)
public class UserDTO {

    private Long id;

    @NotBlank(message = "The field cannot be void")
    private String firstName;

    @NotBlank(message = "The field cannot be void")
    private String lastName;

    private String username;

    private String password;

    private boolean isActive;

    private TraineeDTO traineeDTO;

    private TrainerDTO trainerDTO;

    public static User toEntity(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.getId());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        user.setActive(userDTO.isActive());
        return user;
    }

    public static UserDTO toDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .username(user.getUsername())
                .password(user.getPassword())
                .isActive(user.isActive())
                .build();
    }
}
