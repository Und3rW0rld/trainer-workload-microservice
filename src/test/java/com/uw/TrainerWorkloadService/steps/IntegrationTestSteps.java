package com.uw.TrainerWorkloadService.steps;

import com.uw.TrainerWorkloadService.dto.TrainingRequest;
import com.uw.TrainerWorkloadService.model.TrainerWorkload;
import com.uw.TrainerWorkloadService.repository.TrainerWorkloadRepository;
import com.uw.TrainerWorkloadService.service.TrainerWorkloadManagementService;
import com.uw.TrainerWorkloadService.service.TrainerWorkloadService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class IntegrationTestSteps {

      @Autowired
      private TrainerWorkloadRepository repository;
      @Autowired
      private TrainerWorkloadManagementService trainerWorkloadManagementService;

      private TrainerWorkload trainerWorkload;
      private TrainingRequest trainingRequest;
      @Autowired
      private TrainerWorkloadService trainerWorkloadService;

      @Given("a new training request from Microservice A for trainer with username {string}")
      public void aNewTrainingRequestFromMicroserviceAForTrainerWithUsername(String username) {
            repository.deleteAll();
            trainingRequest = new TrainingRequest();
            trainingRequest.setTrainingDuration(5);
            trainingRequest.setTrainingDate(LocalDate.of( 2021, 1, 1 ));
            trainingRequest.setTrainerUsername(username);
            trainingRequest.setActive(true);
            trainingRequest.setActionType("add");
      }

      @When("the request is processed by Microservice B")
      public void theRequestIsProcessedByMicroserviceB() {
            trainerWorkloadManagementService.processRequest(trainingRequest);
      }

      @Then("the trainer workload should be created or updated in the database")
      public void theTrainerWorkloadShouldBeCreatedOrUpdatedInTheDatabase() {
            assertTrue(trainerWorkloadService.getTrainerWorkloadByUsername(trainingRequest.getTrainerUsername()).isPresent());
      }
}