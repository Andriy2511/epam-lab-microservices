package org.example.finalprojectepamlabapplication.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.example.finalprojectepamlabapplication.DTO.endpointDTO.ChangeLoginRequestDTO;
import org.example.finalprojectepamlabapplication.security.GumUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

public interface UserController {

    @Operation(summary = "Receives user login",
            description = "The method receives the user login by user id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The password changed successfully"),
            @ApiResponse(responseCode = "400", description = "User not found")
    })
    String getUserLogin(@AuthenticationPrincipal GumUserDetails userDetails);

    @Operation(summary = "Changes user password",
        description = "The method changes user password if the user provides a valid old and a new password.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password changed successfully"),
            @ApiResponse(responseCode = "400", description = "User not found"),
            @ApiResponse(responseCode = "401", description = "An old password isn't the same as a new password")
    })
    void changePassword(@AuthenticationPrincipal GumUserDetails userDetails, @ModelAttribute @Valid ChangeLoginRequestDTO changeLoginRequestDTO);


    @Operation(summary = "Changes user status",
            description = "The method changes user active status.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status changed successfully"),
            @ApiResponse(responseCode = "400", description = "User not found")
    })
    void changeStatus(@AuthenticationPrincipal GumUserDetails userDetails);
}
