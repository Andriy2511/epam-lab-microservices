package org.example.finalprojectepamlabapplication.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.example.finalprojectepamlabapplication.DTO.endpointDTO.JwtTokenDTO;
import org.example.finalprojectepamlabapplication.DTO.endpointDTO.LoginRequestDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

public interface LoginController {

    @Operation(summary = "Creates JWT token for users",
            description = "The method creates JWT token for users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The token created successfully"),
            @ApiResponse(responseCode = "400", description = "User not found")
    })
    JwtTokenDTO createAuthToken(@Valid @RequestBody LoginRequestDTO loginRequestDTO);

    @Operation(summary = "Performs logout",
            description = "The method moves JWT token to the blacklist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "logout successfully"),
    })
    void logout(@RequestHeader("Authorization") String authorization);
}
