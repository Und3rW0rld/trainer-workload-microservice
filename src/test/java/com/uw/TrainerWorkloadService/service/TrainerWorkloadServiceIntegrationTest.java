package com.uw.TrainerWorkloadService.service;

import com.uw.TrainerWorkloadService.model.Month;
import com.uw.TrainerWorkloadService.model.TrainerWorkload;
import com.uw.TrainerWorkloadService.model.YearSummary;
import com.uw.TrainerWorkloadService.repository.TrainerWorkloadRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for the TrainerWorkloadService class.
 */
@SpringBootTest
@ActiveProfiles("test")  // Usa el perfil de configuración para pruebas con MongoDB
public class TrainerWorkloadServiceIntegrationTest {

      @Autowired
      private TrainerWorkloadService trainerWorkloadService;

      @Autowired
      private TrainerWorkloadManagementService trainerWorkloadManagementService;

      @Autowired
      private TrainerWorkloadRepository trainerWorkloadRepository;

      @Autowired
      private MongoTemplate mongoTemplate;

      /**
       * Clean up the database after each test.
       */
      @AfterEach
      public void tearDown() {
            trainerWorkloadRepository.deleteAll();
      }

      /**
       * Test to verify that a trainer workload is returned when the trainer exists.
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
      }

      /**
       * Test to verify that the username matches when the trainer exists.
       */
      @Test
      public void testGetTrainerWorkloadByUsername_TrainerExists_UsernameMatches() {
            // Arrange
            String username = "trainer1";
            TrainerWorkload trainerWorkload = new TrainerWorkload();
            trainerWorkload.setTrainerUsername(username);
            trainerWorkloadService.saveTrainerWorkload(trainerWorkload);

            // Act
            Optional<TrainerWorkload> result = trainerWorkloadService.getTrainerWorkloadByUsername(username);

            // Assert
            assertEquals(username, result.get().getTrainerUsername(), "Expected username to match");
      }

      /**
       * Test to verify that a new year entry is created when the year does not exist.
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
            assertEquals("Training added successfully", resultMessage);
      }

      /**
       * Test to verify that the trainer workload is present after adding training when the year does not exist.
       */
      @Test
      public void testAddTraining_YearDoesNotExist_TrainerWorkloadPresent() {
            // Arrange
            String username = "trainer2";
            String firstName = "John";
            String lastName = "Doe";
            boolean isActive = true;
            int year = 2024;
            int month = 1;
            int duration = 5;

            // Act
            trainerWorkloadManagementService.addTraining(username, firstName, lastName, isActive, year, month, duration);

            // Assert
            Optional<TrainerWorkload> trainerWorkloadOpt = trainerWorkloadService.getTrainerWorkloadByUsername(username);
            assertTrue(trainerWorkloadOpt.isPresent(), "Expected trainer workload to be present after training addition");
      }

      /**
       * Test to verify that a year summary is created when the year does not exist.
       */
      @Test
      public void testAddTraining_YearDoesNotExist_YearSummaryCreated() {
            // Arrange
            String username = "trainer2";
            String firstName = "John";
            String lastName = "Doe";
            boolean isActive = true;
            int year = 2024;
            int month = 1;
            int duration = 5;

            // Act
            trainerWorkloadManagementService.addTraining(username, firstName, lastName, isActive, year, month, duration);

            // Assert
            Optional<TrainerWorkload> trainerWorkloadOpt = trainerWorkloadService.getTrainerWorkloadByUsername(username);
            YearSummary addedYear = trainerWorkloadOpt.get().getYears().stream().filter(y -> y.getYear() == year).findFirst().orElse(null);
            assertNotNull(addedYear, "Expected year summary to be created for the new year");
      }

      /**
       * Test to verify that the hours match the added duration when the year does not exist.
       */
      @Test
      public void testAddTraining_YearDoesNotExist_HoursMatch() {
            // Arrange
            String username = "trainer2";
            String firstName = "John";
            String lastName = "Doe";
            boolean isActive = true;
            int year = 2024;
            int month = 1;
            int duration = 5;

            // Act
            trainerWorkloadManagementService.addTraining(username, firstName, lastName, isActive, year, month, duration);

            // Assert
            Optional<TrainerWorkload> trainerWorkloadOpt = trainerWorkloadService.getTrainerWorkloadByUsername(username);
            YearSummary addedYear = trainerWorkloadOpt.get().getYears().stream().filter(y -> y.getYear() == year).findFirst().orElse(null);
            assertEquals(duration, addedYear.getHours(Month.fromNumber(month)), "Expected hours to match the added duration");
      }

      /**
       * Test to verify that a trainer workload is saved successfully.
       */
      @Test
      public void testSaveTrainerWorkload_SavesSuccessfully() {
            // Arrange
            TrainerWorkload trainerWorkload = new TrainerWorkload();
            trainerWorkload.setTrainerUsername("trainer1");

            // Act
            TrainerWorkload savedWorkload = trainerWorkloadService.saveTrainerWorkload(trainerWorkload);

            // Assert
            assertEquals("trainer1", savedWorkload.getTrainerUsername(), "Expected username to match");
      }

      /**
       * Test to verify that a trainer workload is deleted when the trainer exists.
       */
      @Test
      public void testDeleteTrainerWorkload_TrainerExists_DeletesWorkload() {
            // Arrange
            TrainerWorkload trainerWorkload = new TrainerWorkload();
            trainerWorkload.setTrainerUsername("trainer1");
            TrainerWorkload savedWorkload = trainerWorkloadService.saveTrainerWorkload(trainerWorkload);
            String id = savedWorkload.getId();

            // Act
            trainerWorkloadService.deleteTrainerWorkload(id);
            Optional<TrainerWorkload> result = trainerWorkloadService.getTrainerWorkloadById(id);

            // Assert
            assertFalse(result.isPresent(), "Expected workload to be deleted");
      }

      /**
       * Test to verify that a trainer workload is returned by ID when the trainer exists.
       */
      @Test
      public void testGetTrainerWorkloadById_TrainerExists_ReturnsWorkload() {
            // Arrange
            TrainerWorkload trainerWorkload = new TrainerWorkload();
            trainerWorkload.setTrainerUsername("trainer1");
            TrainerWorkload savedWorkload = trainerWorkloadService.saveTrainerWorkload(trainerWorkload);
            String id = savedWorkload.getId();

            // Act
            Optional<TrainerWorkload> result = trainerWorkloadService.getTrainerWorkloadById(id);

            // Assert
            assertEquals(id, result.get().getId(), "Expected ID to match");
      }

      /**
       * Test to verify that all trainer workloads are returned.
       */
      @Test
      public void testGetAllTrainerWorkloads_ReturnsAllWorkloads() {
            // Act
            List<TrainerWorkload> result = trainerWorkloadService.getAllTrainerWorkloads().get();

            // Assert
            assertNotNull(result, "Expected a list of trainer workloads");
            assertFalse(result.isEmpty(), "Expected the list to contain at least one workload");
      }
}
