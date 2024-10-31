package com.uw.TrainerWorkloadService.controller;

import com.uw.TrainerWorkloadService.dto.TrainingRequest;
import com.uw.TrainerWorkloadService.model.Month;
import com.uw.TrainerWorkloadService.model.TrainerWorkload;
import com.uw.TrainerWorkloadService.model.YearSummary;
import com.uw.TrainerWorkloadService.service.TrainerWorkloadManagementService;
import com.uw.TrainerWorkloadService.service.TrainerWorkloadService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the TrainerWorkloadController class.
 */
@ExtendWith(MockitoExtension.class)
class TrainerWorkloadControllerTest {

      private static final String TEST_USERNAME = "testUser";
      private static final int TEST_YEAR = 2024;
      private static final int TEST_MONTH = 10;

      @Mock
      private TrainerWorkloadService trainerWorkloadService;

      @Mock
      private TrainerWorkloadManagementService trainerWorkloadManagementService;

      @InjectMocks
      private TrainerWorkloadController trainerWorkloadController;

      private TrainingRequest trainingRequest;

      /**
       * Sets up the test environment before each test.
       */
      @BeforeEach
      void setup() {
            trainingRequest = new TrainingRequest();
            trainingRequest.setTrainerUsername(TEST_USERNAME);
            trainingRequest.setFirstName("Test");
            trainingRequest.setLastName("User");
            trainingRequest.setActive(true);
            trainingRequest.setTrainingDate(LocalDate.of(TEST_YEAR, TEST_MONTH, 24));
            trainingRequest.setTrainingDuration(2);
      }

      /**
       * Tests that an invalid action type in the training request returns a Bad Request response.
       */
      @Test
      void trainingRequest_InvalidActionType_ReturnsBadRequest() {
            trainingRequest.setActionType("invalid");

            when(trainerWorkloadManagementService.processRequest(trainingRequest))
                    .thenReturn(ResponseEntity.badRequest().body("{\"message\": \"Invalid action type\"}"));

            ResponseEntity<?> response = trainerWorkloadController.trainingRequest(trainingRequest);

            assertEquals(400, response.getStatusCode().value());
            assertEquals("{\"message\": \"Invalid action type\"}", response.getBody());
      }

      /**
       * Tests that a null training request returns a Bad Request response.
       */
      @Test
      void trainingRequest_NullRequestBody_ReturnsBadRequest() {
            ResponseEntity<?> response = trainerWorkloadController.trainingRequest(null);

            assertEquals(400, response.getStatusCodeValue());
            assertEquals("{\"message\": \"Invalid training request\"}", response.getBody());
      }

      /**
       * Tests that a valid request for monthly hours returns the correct number of hours.
       */
      @Test
      void getMonthlyHours_ValidRequest_ReturnsHours() {
            TrainerWorkload trainerWorkload = new TrainerWorkload();
            trainerWorkload.setTrainerUsername(TEST_USERNAME);
            YearSummary yearSummary = new YearSummary();
            yearSummary.setYear(TEST_YEAR);
            yearSummary.addHours(Month.OCTOBER, 10);
            trainerWorkload.getYears().add(yearSummary);

            when(trainerWorkloadService.getTrainerWorkloadByUsername(anyString())).thenReturn(Optional.of(trainerWorkload));
            when(trainerWorkloadManagementService.getMonthlyHours(anyString(), anyInt(), anyInt())).thenReturn(10);

            ResponseEntity<?> response = trainerWorkloadController.getMonthlyHours(TEST_USERNAME, TEST_YEAR, TEST_MONTH);

            assertEquals(200, response.getStatusCode().value());
            assertEquals("{\"hours\": 10}", response.getBody());
      }

      /**
       * Tests that a request for monthly hours with an invalid user returns a Not Found response.
       */
      @Test
      void getMonthlyHours_InvalidUser_ReturnsNotFound() {
            when(trainerWorkloadService.getTrainerWorkloadByUsername(anyString())).thenReturn(Optional.empty());

            ResponseEntity<?> response = trainerWorkloadController.getMonthlyHours("invalidUser", TEST_YEAR, TEST_MONTH);

            assertEquals(404, response.getStatusCode().value());
            assertEquals("{\"message\": \"User not found\"}", response.getBody());
      }

      /**
       * Tests that a request to add training returns a success message.
       */
      @Test
      void trainingRequest_AddTraining_ReturnsSuccessMessage() {
            trainingRequest.setActionType("add");

            when(trainerWorkloadManagementService.processRequest(trainingRequest))
                    .thenReturn(ResponseEntity.ok().body("{\"message\": \"Training added successfully\"}"));

            ResponseEntity<?> response = trainerWorkloadController.trainingRequest(trainingRequest);

            assertEquals(200, response.getStatusCode().value());
            assertEquals("{\"message\": \"Training added successfully\"}", response.getBody());
      }

      /**
       * Tests that a request to delete training returns a success message.
       */
      @Test
      void trainingRequest_DeleteTraining_ReturnsSuccessMessage() {
            trainingRequest.setActionType("delete");

            when(trainerWorkloadManagementService.processRequest(trainingRequest))
                    .thenReturn(ResponseEntity.ok().body("{\"message\": \"Training deleted successfully\"}"));

            ResponseEntity<?> response = trainerWorkloadController.trainingRequest(trainingRequest);

            assertEquals(200, response.getStatusCode().value());
            assertEquals("{\"message\": \"Training deleted successfully\"}", response.getBody());
      }

}