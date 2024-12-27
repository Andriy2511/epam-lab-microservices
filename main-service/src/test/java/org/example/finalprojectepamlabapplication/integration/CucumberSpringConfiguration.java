package org.example.finalprojectepamlabapplication.integration;

import org.example.finalprojectepamlabapplication.FinalProjectEpamLabApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = FinalProjectEpamLabApplication.class)
@ContextConfiguration
public class CucumberSpringConfiguration {

}
