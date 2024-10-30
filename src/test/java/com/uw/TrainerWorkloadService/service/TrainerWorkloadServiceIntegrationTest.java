package com.uw.TrainerWorkloadService.service;

import com.uw.TrainerWorkloadService.model.Month;
import com.uw.TrainerWorkloadService.model.TrainerWorkload;
import com.uw.TrainerWorkloadService.model.YearSummary;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for the TrainerWorkloadService class.
 */
@SpringBootTest
@Transactional
public class TrainerWorkloadServiceIntegrationTest {

      @Autowired
      private TrainerWorkloadService trainerWorkloadService;

      @Autowired
      private TrainerWorkloadManagementService trainerWorkloadManagementService;

      /**
       * Tests retrieving a TrainerWorkload by username when the trainer exists.
       */
      @Test
      public void testGetTrainerWorkloadByUsername_TrainerExists_ReturnsWorkload() {
            // Arrange
            String username = "trainer1";
            TrainerWorkload trainerWorkload = new TrainerWorkload();
            trainerWorkload.setTrainerUsername(username);
            trainerWorkloadService.saveTrainerWorkload(trainerWorkload);

            // Act
            Optional<TrainerWorkload> result = trainerWorkloadService.getTrainerWorkloadByUsername(username);

            // Assert
            assertTrue(result.isPresent(), "Expected workload to be present");
            assertEquals(username, result.get().getTrainerUsername(), "Expected username to match");
      }

      /**
       * Tests adding training hours when the year does not exist, creating a new year entry.
       */
      @Test
      public void testAddTraining_YearDoesNotExist_CreatesNewYearEntry() {
            // Arrange
            String username = "trainer2";
            String firstName = "John";
            String lastName = "Doe";
            boolean isActive = true;
            int year = 2024;
            int month = 1;
            int duration = 5;

            // Act
            String resultMessage = trainerWorkloadManagementService.addTraining(username, firstName, lastName, isActive, year, month, duration);

            // Assert
            Optional<TrainerWorkload> trainerWorkloadOpt = trainerWorkloadService.getTrainerWorkloadByUsername(username);
            assertTrue(trainerWorkloadOpt.isPresent(), "Expected trainer workload to be present after training addition");
            assertEquals("Training added successfully", resultMessage);
            YearSummary addedYear = trainerWorkloadOpt.get().getYears().stream().filter(y -> y.getYear() == year).findFirst().orElse(null);
            assertNotNull(addedYear, "Expected year summary to be created for the new year");
            assertEquals(duration, addedYear.getHours(Month.fromNumber(month)), "Expected hours to match the added duration");
      }

      /**
       * Tests saving a TrainerWorkload entity successfully.
       */
      @Test
      public void testSaveTrainerWorkload_SavesSuccessfully() {
            // Arrange
            TrainerWorkload trainerWorkload = new TrainerWorkload();
            trainerWorkload.setTrainerUsername("trainer1");

            // Act
            TrainerWorkload savedWorkload = trainerWorkloadService.saveTrainerWorkload(trainerWorkload);

            // Assert
            assertNotNull(savedWorkload, "Expected workload to be saved");
            assertEquals("trainer1", savedWorkload.getTrainerUsername(), "Expected username to match");
      }

      /**
       * Tests deleting a TrainerWorkload by ID when the trainer exists.
       */
      @Test
      public void testDeleteTrainerWorkload_TrainerExists_DeletesWorkload() {
            // Arrange
            TrainerWorkload trainerWorkload = new TrainerWorkload();
            trainerWorkload.setTrainerUsername("trainer1");
            TrainerWorkload savedWorkload = trainerWorkloadService.saveTrainerWorkload(trainerWorkload);
            long id = savedWorkload.getId();

            // Act
            trainerWorkloadService.deleteTrainerWorkload(id);
            Optional<TrainerWorkload> result = trainerWorkloadService.getTrainerWorkloadById(id);

            // Assert
            assertFalse(result.isPresent(), "Expected workload to be deleted");
      }

      /**
       * Tests retrieving a TrainerWorkload by ID when the trainer exists.
       */
      @Test
      public void testGetTrainerWorkloadById_TrainerExists_ReturnsWorkload() {
            // Arrange
            TrainerWorkload trainerWorkload = new TrainerWorkload();
            trainerWorkload.setTrainerUsername("trainer1");
            TrainerWorkload savedWorkload = trainerWorkloadService.saveTrainerWorkload(trainerWorkload);
            long id = savedWorkload.getId();

            // Act
            Optional<TrainerWorkload> result = trainerWorkloadService.getTrainerWorkloadById(id);

            // Assert
            assertTrue(result.isPresent(), "Expected workload to be present");
            assertEquals(id, result.get().getId(), "Expected ID to match");
      }

      /**
       * Tests retrieving all TrainerWorkload entities.
       */
      @Test
      public void testGetAllTrainerWorkloads_ReturnsAllWorkloads() {
            // Arrange
            TrainerWorkload trainerWorkload1 = new TrainerWorkload();
            trainerWorkload1.setTrainerUsername("trainer1");
            trainerWorkloadService.saveTrainerWorkload(trainerWorkload1);

            TrainerWorkload trainerWorkload2 = new TrainerWorkload();
            trainerWorkload2.setTrainerUsername("trainer2");
            trainerWorkloadService.saveTrainerWorkload(trainerWorkload2);

            // Act
            Optional<List<TrainerWorkload>> result = trainerWorkloadService.getAllTrainerWorkloads();

            // Assert
            assertTrue(result.isPresent(), "Expected workloads to be present");
            assertEquals(2, result.get().size(), "Expected two workloads to be present");
      }

}