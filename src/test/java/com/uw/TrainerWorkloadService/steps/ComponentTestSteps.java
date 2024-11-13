package com.uw.TrainerWorkloadService.steps;

import com.uw.TrainerWorkloadService.dto.TrainingRequest;
import com.uw.TrainerWorkloadService.model.TrainerWorkload;
import com.uw.TrainerWorkloadService.model.YearSummary;
import com.uw.TrainerWorkloadService.repository.TrainerWorkloadRepository;
import com.uw.TrainerWorkloadService.service.TrainerWorkloadManagementService;
import com.uw.TrainerWorkloadService.service.TrainerWorkloadService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.BeforeEach;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ComponentTestSteps {

      @Autowired
      private TrainerWorkloadManagementService trainerWorkloadManagementService;
      @Autowired
      private TrainerWorkloadService trainerWorkloadService;
      @Autowired
      private TrainerWorkloadRepository repository;

      private TrainingRequest trainingRequest;
      private TrainerWorkload trainerWorkload;



      @Given("a new training request for trainer with username {string}")
      public void aNewTrainingRequestForTrainerWithUsername(String username) {
            repository.deleteAll();
            trainingRequest = createTrainingRequest(username, LocalDate.of(2021, 1, 1), 5, true, "add");
      }

      @When("the request is processed")
      public void theRequestIsProcessed() {
            trainerWorkloadManagementService.addTraining(trainingRequest);
      }

      @Then("a new trainer workload should be created in the database")
      public void aNewTrainerWorkloadShouldBeCreatedInTheDatabase() {
            assertNotNull(repository.findByTrainerUsername(trainingRequest.getTrainerUsername()).orElse(null),
                    "Trainer workload should be created in the database");
      }

      @Given("an existing trainer workload for trainer with username {string}")
      public void anExistingTrainerWorkloadForTrainerWithUsername(String username) {
            repository.deleteAll();
            trainerWorkload = new TrainerWorkload();
            trainerWorkload.setTrainerUsername(username);
            trainerWorkload.setYears( List.of(new YearSummary(2021)));
            repository.save(trainerWorkload);
            trainingRequest = createTrainingRequest(username, LocalDate.of(2021, 1, 1), 5, true, "add");
      }

      @When("an add hours request is processed")
      public void anAddHoursRequestIsProcessed() {
            trainingRequest.setActionType("add");
            trainerWorkloadManagementService.processRequest(trainingRequest);
      }

      @Then("the trainer workload should be updated in the database")
      public void theTrainerWorkloadShouldBeUpdatedInTheDatabase() {
            TrainerWorkload updatedTrainerWorkload = trainerWorkloadService.getTrainerWorkloadByUsername(trainerWorkload.getTrainerUsername()).orElse(null);
            assertNotNull(updatedTrainerWorkload, "Trainer workload should be updated");
            assertFalse(updatedTrainerWorkload.getYears().isEmpty(), "Trainer workload should have updated years data");
      }

      @When("a remove hours request is processed")
      public void aRemoveHoursRequestIsProcessed() {
            trainingRequest.setActionType("delete");
            trainerWorkloadManagementService.processRequest(trainingRequest);
      }

      @Then("the trainer workload should be updated in the database with the hours removed")
      public void theTrainerWorkloadShouldBeUpdatedInTheDatabaseWithTheHoursRemoved() {
            TrainerWorkload updatedTrainerWorkload = trainerWorkloadService.getTrainerWorkloadByUsername(trainerWorkload.getTrainerUsername()).orElse(null);
            assertNotNull(updatedTrainerWorkload, "Trainer workload should be updated with hours removed");
      }

      private TrainingRequest createTrainingRequest(String username, LocalDate date, int duration, boolean isActive, String actionType) {
            TrainingRequest request = new TrainingRequest();
            request.setTrainerUsername(username);
            request.setTrainingDate(date);
            request.setTrainingDuration(duration);
            request.setActive(isActive);
            request.setActionType(actionType);
            return request;
      }
}
