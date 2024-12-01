package org.example.finalprojectepamlabapplication.controller.implementation;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.example.finalprojectepamlabapplication.DTO.modelDTO.TrainingTypeDTO;
import org.example.finalprojectepamlabapplication.defaulttestdata.dto.DTOBuilder;
import org.example.finalprojectepamlabapplication.service.TrainingTypeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TrainingTypeControllerImplTest {

    @InjectMocks
    TrainingTypeControllerImpl trainingTypeController;

    @Mock
    TrainingTypeService trainingTypeService;

    TrainingTypeDTO firstTrainingTypeDTO;
    TrainingTypeDTO secondTrainingTypeDTO;
    List<TrainingTypeDTO> trainingTypeDTOList;

    @BeforeEach
    public void setUp() {
        RestAssuredMockMvc.standaloneSetup(trainingTypeController);
        firstTrainingTypeDTO = DTOBuilder.buildTrainingTypeDTO(1L, "firstTestTrainingType");
        secondTrainingTypeDTO = DTOBuilder.buildTrainingTypeDTO(2L, "secondTestTrainingType");

        trainingTypeDTOList = new ArrayList<>();
        trainingTypeDTOList.add(firstTrainingTypeDTO);
        trainingTypeDTOList.add(secondTrainingTypeDTO);

    }

    @Test
    public void getAllTrainingTypesTest(){
        when(trainingTypeService.getAllTrainingTypes()).thenReturn(trainingTypeDTOList);

        given()
                .when()
                    .get("/training-types")
                .then()
                    .statusCode(200)
                    .body("size()", is(2))
                    .body("[0].id", equalTo(1))
                    .body("[0].trainingTypeName", equalTo("firstTestTrainingType"))
                    .body("[1].id", equalTo(2))
                    .body("[1].trainingTypeName", equalTo("secondTestTrainingType"));
    }
}
