package org.example.finalprojectepamlabapplication.DTO.endpointDTO;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginRequestDTO {

    @NotBlank(message = "The field cannot be void")
    private String username;

    @NotBlank(message = "The field cannot be void")
    private String password;

}
