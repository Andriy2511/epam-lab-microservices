package org.example.finalprojectepamlabapplication.controller.implementation;

import org.example.finalprojectepamlabapplication.DTO.endpointDTO.ChangeLoginRequestDTO;
import org.example.finalprojectepamlabapplication.DTO.modelDTO.TraineeDTO;
import org.example.finalprojectepamlabapplication.DTO.modelDTO.UserDTO;
import org.example.finalprojectepamlabapplication.defaulttestdata.dto.DTOBuilder;
import org.example.finalprojectepamlabapplication.exception.UnauthorizedException;
import org.example.finalprojectepamlabapplication.model.User;
import org.example.finalprojectepamlabapplication.security.GumUserDetails;
import org.example.finalprojectepamlabapplication.service.UserService;
import org.example.finalprojectepamlabapplication.service.implementation.UserDetailsLoaderImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
public class UserControllerImplTest {

    @Mock
    private UserService userService;

    @Mock
    private UserDetailsLoaderImpl userDetailsLoader;

    @InjectMocks
    private UserControllerImpl userController;

    private UserDTO userDTO;
    private TraineeDTO traineeDTO;
    private GumUserDetails userDetails;

    @BeforeEach
    public void setUp() {
        initializeValues();
    }

    @Test
    public void getUserLoginTest() {
        lenient().when(userService.getUserByUsername(anyString())).thenReturn(userDTO);

        String result = userController.getUserLogin(userDetails);

        assertNotNull(result);
        assertEquals(userDTO.getUsername(), result);
    }

    @Test
    public void changePasswordSuccessTest() {
        ChangeLoginRequestDTO changeLoginRequestDTO = new ChangeLoginRequestDTO();
        changeLoginRequestDTO.setOldPassword("oldPass");
        changeLoginRequestDTO.setNewPassword("newPass");

        when(userService.getUserByUsername(userDTO.getUsername())).thenReturn(userDTO);
        when(userService.isOldPasswordSimilarToCurrentPassword(1L, "oldPass")).thenReturn(true);

        userController.changePassword(userDetails, changeLoginRequestDTO);

        verify(userService, times(1)).updateUserPassword(userDTO, "newPass");
    }

    @Test
    public void changePasswordFailureTest() {
        ChangeLoginRequestDTO changeLoginRequestDTO = new ChangeLoginRequestDTO();
        changeLoginRequestDTO.setOldPassword("wrongPass");
        changeLoginRequestDTO.setNewPassword("newPass");

        when(userService.getUserByUsername(userDTO.getUsername())).thenReturn(userDTO);
        when(userService.isOldPasswordSimilarToCurrentPassword(1L, "wrongPass")).thenReturn(false);

        UnauthorizedException exception = assertThrows(
                UnauthorizedException.class,
                () -> userController.changePassword(userDetails, changeLoginRequestDTO)
        );

        assertEquals("The old password is incorrect", exception.getMessage());
        verify(userService, never()).updateUserPassword(any(), anyString());
    }

    @Test
    public void changeStatusTest() {
        when(userService.getUserByUsername(userDTO.getUsername())).thenReturn(userDTO);

        assertDoesNotThrow(() -> userController.changeStatus(userDetails));
        verify(userService, times(1)).changeActiveStatus(1L);
    }

    private void initializeValues(){
        userDTO = DTOBuilder.buildUserDTO(1L);
        traineeDTO = DTOBuilder.buildTraineeDTO(1L, userDTO);
        userDTO.toBuilder().traineeDTO(traineeDTO).build();
        initializeUserDetails();
    }

    private void initializeUserDetails(){
        User user = UserDTO.toEntity(traineeDTO.getUserDTO());
        user.setTrainee(TraineeDTO.toEntity(traineeDTO));
        userDetails = new GumUserDetails(user);
    }
}
