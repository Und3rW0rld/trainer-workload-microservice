package com.uw.TrainerWorkloadService.steps;

import com.uw.TrainerWorkloadService.dto.TrainingRequest;
import com.uw.TrainerWorkloadService.model.Month;
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
import org.springframework.http.ResponseEntity;

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
      private ResponseEntity<String> response;


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

      @When("an add hours request with invalid data is processed")
      public void anAddHoursRequestWithInvalidDataIsProcessed() {
            trainingRequest.setTrainingDuration(-5); // Invalid duration
            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                  trainerWorkloadManagementService.processRequest(trainingRequest);
            });
            assertTrue(exception.getMessage().contains("Invalid training duration"), "Validation error expected");
      }

      @Then("the trainer workload should not be updated in the database")
      public void theTrainerWorkloadShouldNotBeUpdatedInTheDatabase() {
            TrainerWorkload updatedTrainerWorkload = trainerWorkloadService.getTrainerWorkloadByUsername(trainerWorkload.getTrainerUsername()).orElse(null);
            assertNotNull(updatedTrainerWorkload, "Trainer workload should exist");

            // Buscar el resumen del año 2021
            YearSummary yearSummary = updatedTrainerWorkload.getYears().stream()
                    .filter(summary -> summary.getYear() == 2021)
                    .findFirst()
                    .orElse(null);
            assertNotNull(yearSummary, "Year summary for 2021 should exist");

            // Verificar las horas en enero
            int januaryHours = yearSummary.getHours(Month.JANUARY); // Enero es el mes 1
            assertEquals(0, januaryHours, "Trainer workload hours for January 2021 should remain unchanged");
      }


      @Given("an invalid training request with missing or incorrect fields")
      public void anInvalidTrainingRequestWithMissingOrIncorrectFields() {
            trainingRequest = new TrainingRequest();
            trainingRequest.setTrainerUsername(null); // Falta el nombre del entrenador
            trainingRequest.setTrainingDate(LocalDate.of(2021, 3, 1));
            trainingRequest.setTrainingDuration(-5); // Duración negativa
            trainingRequest.setActionType("add");
      }
      @When("the bad request is processed")
      public void theRequestBadIsProcessed() {
            response = trainerWorkloadManagementService.processRequest(trainingRequest);

      }
      @Then("the system should return an error response")
      public void theSystemShouldReturnAnErrorResponse() {
            assertNotNull(response, "ResponseEntity should not be null");
            assertEquals(400, response.getStatusCodeValue(), "HTTP status should be 400 Bad Request");
      }

      @Then("an error message should indicate the reason for the invalid request")
      public void anErrorMessageShouldIndicateTheReasonForTheInvalidRequest() {
            assertNotNull(response.getBody(), "Response body should not be null");
            assertTrue(response.getBody().toString().contains("Trainer username must not be blank"),
                    "Response body should contain the expected error message");
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
