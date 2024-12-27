package org.example.finalprojectepamlabapplication.integration.stepdefinitions;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import org.example.finalprojectepamlabapplication.DTO.endpointDTO.LoginRequestDTO;
import org.example.finalprojectepamlabapplication.DTO.modelDTO.TraineeDTO;
import org.example.finalprojectepamlabapplication.DTO.modelDTO.TrainerDTO;
import org.example.finalprojectepamlabapplication.DTO.modelDTO.TrainingTypeDTO;
import org.example.finalprojectepamlabapplication.DTO.modelDTO.UserDTO;
import org.example.finalprojectepamlabapplication.defaulttestdata.dto.DTOBuilder;
import org.example.finalprojectepamlabapplication.integration.stepdefinitions.hooks.TestHooks;
import io.restassured.response.Response;

import static org.hamcrest.Matchers.notNullValue;

public class AuthorizationSteps {

    private TrainingTypeDTO trainingTypeDTO;
    private TrainerDTO trainerDTO;
    private TraineeDTO traineeDTO;
    private UserDTO userDTO;

    private String generatedUsername;
    private String generatedPassword;

    private String userTypeTrainer;
    private String userTypeTrainee;

    public AuthorizationSteps() {
        trainingTypeDTO = DTOBuilder.buildTrainingTypeDTO(null, "Strength");
        userDTO = DTOBuilder.buildUserDTO(null);
        traineeDTO = DTOBuilder.buildTraineeDTO(null, userDTO);
        trainerDTO = DTOBuilder.buildTrainerDTO(null, userDTO, trainingTypeDTO);
        userTypeTrainer = "trainer";
        userTypeTrainee = "trainee";
    }

    @When("a user registers as a trainer with first name {string} and last name {string}")
    public void registerAsTrainer(String firstName, String lastName){
        UserDTO user = userDTO.toBuilder()
                .firstName(firstName)
                .lastName(lastName)
                .build();

        TrainerDTO trainer = trainerDTO.toBuilder().userDTO(user).build();

        TestHooks.response = RestAssured.given()
                .contentType("application/json")
                .body(trainer)
                .post("/registration/trainer");

        setUserCredential(TestHooks.response, userTypeTrainer);
    }

    @When("a user registers as a trainee with first name {string} and last name {string}")
    public void registerAsTrainee(String firstName, String lastName){
        UserDTO user = userDTO.toBuilder()
                .firstName(firstName)
                .lastName(lastName)
                .build();

        TraineeDTO trainee = traineeDTO.toBuilder().userDTO(user).build();

        TestHooks.response = RestAssured.given()
                .contentType("application/json")
                .body(trainee)
                .post("/registration/trainee");

        setUserCredential(TestHooks.response, userTypeTrainee);
    }

    @When("a user login with username {string} and password {string}")
    public void login(String username, String password){
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO(username, password);

        TestHooks.response = RestAssured.given()
                .contentType("application/json")
                .body(loginRequestDTO)
                .post("/login/authorization");
    }

    @When("a user login with registered credentials")
    public void loginWithCredentials() {
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO(generatedUsername, generatedPassword);

        TestHooks.response = RestAssured.given()
                .contentType("application/json")
                .body(loginRequestDTO)
                .post("/login/authorization");
    }

    @Then("the response should contain a valid token")
    public void verifyToken() {
        TestHooks.token = TestHooks.response.then()
                .body("token", notNullValue())
                .extract()
                .path("token");
    }

    private UserDTO getUserDTOFromResponse(Response response, String userType){
        return userType.equals(userTypeTrainer) ? getUserDtoFromResponseWithTrainerDto(response)
                : getUserDtoFromResponseWithTraineeDto(response);
    }

    private UserDTO getUserDtoFromResponseWithTrainerDto(Response response){
        TrainerDTO trainerDTO = response.as(TrainerDTO.class);
        return trainerDTO.getUserDTO();
    }

    private UserDTO getUserDtoFromResponseWithTraineeDto(Response response){
        TraineeDTO trainerDTO = response.as(TraineeDTO.class);
        return trainerDTO.getUserDTO();
    }

    private void receiveUserCredential(UserDTO user){
        generatedUsername = user.getUsername();
        generatedPassword = user.getPassword();
    }

    private void setUserCredential(Response response, String userType){
        if(response.statusCode() == 200) {
            receiveUserCredential(getUserDTOFromResponse(TestHooks.response, userType));
        }
    }
}
