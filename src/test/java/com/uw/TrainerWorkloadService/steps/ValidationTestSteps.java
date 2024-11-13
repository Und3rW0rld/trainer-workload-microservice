package com.uw.TrainerWorkloadService.steps;

import com.uw.TrainerWorkloadService.dto.TrainingRequest;
import com.uw.TrainerWorkloadService.repository.TrainerWorkloadRepository;
import com.uw.TrainerWorkloadService.service.TrainerWorkloadManagementService;
import com.uw.TrainerWorkloadService.service.TrainerWorkloadService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
public class ValidationTestSteps {

      @Autowired
      private TrainerWorkloadRepository repository;
      @Autowired
      private TrainerWorkloadManagementService trainerWorkloadManagementService;

      private TrainingRequest trainingRequest;
      @Autowired
      private TrainerWorkloadService trainerWorkloadService;

      @Given("an invalid training request with empty username")
      public void anInvalidTrainingRequestWithEmptyUsername() {
            repository.deleteAll();
            trainingRequest = new TrainingRequest();
            trainingRequest.setTrainingDuration(5);
            trainingRequest.setTrainingDate(LocalDate.of(2021, 1, 1));
            trainingRequest.setActive(true);
            trainingRequest.setActionType("add");
      }

      @When("the invalid request is processed")
      public void theInvalidRequestIsProcessed() {
            trainerWorkloadManagementService.processRequest(trainingRequest);
      }

      @Then("the trainer workload should not be created in the database")
      public void theTrainerWorkloadShouldNotBeCreatedInTheDatabase() {
            assertFalse(trainerWorkloadService.getTrainerWorkloadByUsername(trainingRequest.getTrainerUsername()).isPresent());
      }
}