package org.example.finalprojectepamlabapplication.integration.stepdefinitions;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.example.finalprojectepamlabapplication.DTO.endpointDTO.AddTrainingRequestDTO;
import org.example.finalprojectepamlabapplication.DTO.endpointDTO.TrainingMonthSummaryResponseDTO;
import org.example.finalprojectepamlabapplication.integration.stepdefinitions.hooks.TestHooks;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import org.junit.jupiter.api.Assertions;

public class TrainingSteps {

    private AddTrainingRequestDTO addTrainingRequestDTO;
    private Integer totalDuration;

    public TrainingSteps(){
        initializeTraining();
    }

    @When("a trainer with username {string} want to add training with duration {int} for trainee with username {string}")
    public void addTrainingForTrainee(String trainerUsername, int duration, String traineeUsername) {
        addTrainingRequestDTO.setTrainerUsername(trainerUsername);
        addTrainingRequestDTO.setTrainingDuration(duration);

        TestHooks.response = RestAssured.given()
                .contentType("application/x-www-form-urlencoded")
                .header("Authorization", "Bearer " + TestHooks.token)
                .formParam("trainerUsername", addTrainingRequestDTO.getTrainerUsername())
                .formParam("trainingName", addTrainingRequestDTO.getTrainingName())
                .formParam("trainingTypeName", addTrainingRequestDTO.getTrainingTypeName())
                .formParam("trainingDate",  new SimpleDateFormat("yyyy-MM-dd").format(addTrainingRequestDTO.getTrainingDate()))
                .formParam("trainingDuration", addTrainingRequestDTO.getTrainingDuration())
                .post("/trainings/" + traineeUsername);

    }

    @When("a trainer requests his workload with current year and month")
    public void getTrainerWorkload(){

        resetTotalDuration();

        TestHooks.response = RestAssured.given()
                .contentType("application/x-www-form-urlencoded")
                .header("Authorization", "Bearer " + TestHooks.token)
                .formParam("year", getYearAsInteger(new Date()))
                .formParam("month", getMonthAsInteger(new Date()))
                .get("/trainers/workload");

        setTotalDuration(TestHooks.response);


    }

    @Then("the totalDuration should be {int}")
    public void verifyTotalDuration(int expectedTotalDuration) {
        Assertions.assertEquals(expectedTotalDuration, totalDuration);
    }

    private void initializeTraining(){
        addTrainingRequestDTO = new AddTrainingRequestDTO();
        addTrainingRequestDTO.setTrainerUsername("Test");
        addTrainingRequestDTO.setTrainingName("Cardio");
        addTrainingRequestDTO.setTrainingDate(new Date());
        addTrainingRequestDTO.setTrainingTypeName("Cardio");
        addTrainingRequestDTO.setTrainingDuration(0);
    }

    @When("I wait for {int} second(s)")
    public void waitForSeconds(int seconds) throws InterruptedException {
        Thread.sleep(seconds * 1000);
    }

    private int getYearAsInteger(Date date) {
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return localDate.getYear();
    }

    private int getMonthAsInteger(Date date) {
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return localDate.getMonthValue();
    }

    private void setTotalDuration(Response response){
        TrainingMonthSummaryResponseDTO responseDTO = response.as(TrainingMonthSummaryResponseDTO.class);
        totalDuration = responseDTO.getTotalDuration();
    }

    private void resetTotalDuration(){
        totalDuration = null;
    }
}
