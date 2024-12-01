package org.example.finalprojectepamlabapplication.DTO.endpointDTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangeLoginRequestDTO {
    @NotNull(message = "Old password is required")
    @Size(min = 1, message = "Old password cannot be empty")
    private String oldPassword;

    @NotNull(message = "New password is required")
    @Size(min = 1, message = "New password cannot be empty")
    private String newPassword;
}
