package org.example.finalprojectepamlabapplication.controller.implementation;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.example.finalprojectepamlabapplication.DTO.endpointDTO.ChangeLoginRequestDTO;
import org.example.finalprojectepamlabapplication.DTO.modelDTO.UserDTO;
import org.example.finalprojectepamlabapplication.defaulttestdata.dto.DTOBuilder;
import org.example.finalprojectepamlabapplication.exception.UnauthorizedException;
import org.example.finalprojectepamlabapplication.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserControllerImplTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserControllerImpl userController;

    private UserDTO userDTO;

    @BeforeEach
    public void setUp() {
        RestAssuredMockMvc.standaloneSetup(userController);
        userDTO = DTOBuilder.buildUserDTO(1L);
    }

    @Test
    public void getUserLoginTest(){
        when(userService.getUserById(1L)).thenReturn(userDTO);

        given()
                .when()
                    .get("/users/1")
                .then()
                    .statusCode(200)
                    .body(equalTo(userDTO.getUsername()));
    }

    @Test
    public void testChangePasswordSuccessful() {
        String oldPassword = "0123456789";
        String newPassword = "1234567890";

        userDTO.toBuilder().password(oldPassword);
        when(userService.getUserById(1L)).thenReturn(userDTO);
        when(userService.isOldPasswordSimilarToCurrentPassword(anyLong(), anyString())).thenReturn(true);

        given()
                .formParam("newPassword", newPassword)
                .formParam("oldPassword", oldPassword)
                .when()
                    .put("/users/1")
                .then()
                    .statusCode(200);
    }

    @Test
    public void testChangePasswordUnauthorized() {
        String oldPassword = "0123456789";
        String newPassword = "1234567890";

        userDTO.toBuilder().password(oldPassword);
        lenient().when(userService.getUserById(1L)).thenReturn(userDTO);

        assertThrows(UnauthorizedException.class, () -> {
            userController.changePassword(1L, new ChangeLoginRequestDTO("qwertyuiop", newPassword));
        });
    }

    @Test
    public void changeStatusTest(){
        given().when().patch("/users/1").then().statusCode(200);
    }
}
