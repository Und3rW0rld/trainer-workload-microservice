package com.uw.TrainerWorkloadService.service;

import com.uw.TrainerWorkloadService.model.Month;
import com.uw.TrainerWorkloadService.model.TrainerWorkload;
import com.uw.TrainerWorkloadService.model.YearSummary;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the TrainerWorkloadManagementService class.
 */
@ExtendWith(MockitoExtension.class)
public class TrainerWorkloadManagementServiceTest {

      @Mock
      private TrainerWorkloadService trainerWorkloadService;

      @InjectMocks
      private TrainerWorkloadManagementService trainerWorkloadManagementService;

      /**
       * Tests adding training hours to an existing year in the trainer's workload.
       */
      @Test
      public void testAddTraining_YearExists_AddsHoursToExistingYear() {
            // Arrange
            String username = "trainer1";
            int year = 2024;
            int month = 1;
            int duration = 5;
            TrainerWorkload trainerWorkload = new TrainerWorkload();
            YearSummary existingYearSummary = new YearSummary(year);
            trainerWorkload.getYears().add(existingYearSummary);

            when(trainerWorkloadService.getTrainerWorkloadByUsername(username))
                    .thenReturn(Optional.of(trainerWorkload));

            // Act
            String result = trainerWorkloadManagementService.addTraining(username, "John", "Doe", true, year, month, duration);

            // Assert
            verify(trainerWorkloadService, times(1)).saveTrainerWorkload(trainerWorkload);
      }

      /**
       * Tests adding training hours to a new year entry in the trainer's workload.
       */
      @Test
      public void testAddTraining_YearDoesNotExist_CreatesNewYearEntry() {
            // Arrange
            String username = "trainer2";
            int year = 2025;
            int month = 2;
            int duration = 3;
            TrainerWorkload trainerWorkload = new TrainerWorkload();

            when(trainerWorkloadService.getTrainerWorkloadByUsername(username))
                    .thenReturn(Optional.of(trainerWorkload));

            // Act
            trainerWorkloadManagementService.addTraining(username, "John", "Doe", true, year, month, duration);

            // Assert
            trainerWorkload.getYears().stream().filter(y -> y.getYear() == year).findFirst().orElse(null);
            verify(trainerWorkloadService, times(1)).saveTrainerWorkload(trainerWorkload);
      }

      /**
       * Tests adding training hours for a trainer that does not exist, creating a new TrainerWorkload.
       */
      @Test
      public void addTraining_TrainerDoesNotExist_CreatesNewTrainerWorkload() {
            String username = "trainer3";
            int year = 2023;
            int month = 3;
            int duration = 4;

            when(trainerWorkloadService.getTrainerWorkloadByUsername(username))
                    .thenReturn(Optional.empty());

            trainerWorkloadManagementService.addTraining(username, "Jane", "Doe", true, year, month, duration);

            verify(trainerWorkloadService, times(1)).saveTrainerWorkload(any(TrainerWorkload.class));
      }

      /**
       * Tests deleting training hours from an existing year in the trainer's workload.
       */
      @Test
      public void deleteTraining_TrainerExists_DeletesHoursFromExistingYear() {
            String username = "trainer1";
            int year = 2024;
            int month = 1;
            int duration = 5;
            TrainerWorkload trainerWorkload = new TrainerWorkload();
            YearSummary existingYearSummary = new YearSummary(year);
            existingYearSummary.addHours(Month.fromNumber(month), duration);
            trainerWorkload.getYears().add(existingYearSummary);

            when(trainerWorkloadService.getTrainerWorkloadByUsername(username))
                    .thenReturn(Optional.of(trainerWorkload));

            String result = trainerWorkloadManagementService.deleteTraining(username, year, month, duration);

            verify(trainerWorkloadService, times(1)).saveTrainerWorkload(trainerWorkload);
      }

      /**
       * Tests deleting training hours for a trainer that does not exist, expecting an exception.
       */
      @Test
      public void deleteTraining_TrainerDoesNotExist_ThrowsException() {
            String username = "trainer4";
            int year = 2023;
            int month = 3;
            int duration = 4;

            when(trainerWorkloadService.getTrainerWorkloadByUsername(username))
                    .thenReturn(Optional.empty());

            assertThrows(IllegalArgumentException.class, () -> {
                  trainerWorkloadManagementService.deleteTraining(username, year, month, duration);
            });
      }

      /**
       * Tests retrieving monthly training hours for an existing trainer.
       */
      @Test
      public void getMonthlyHours_TrainerExists_ReturnsCorrectHours() {
            String username = "trainer1";
            int year = 2024;
            int month = 1;
            int duration = 5;
            TrainerWorkload trainerWorkload = new TrainerWorkload();
            YearSummary existingYearSummary = new YearSummary(year);
            existingYearSummary.addHours(Month.fromNumber(month), duration);
            trainerWorkload.getYears().add(existingYearSummary);

            when(trainerWorkloadService.getTrainerWorkloadByUsername(username))
                    .thenReturn(Optional.of(trainerWorkload));

            int result = trainerWorkloadManagementService.getMonthlyHours(username, year, month);

            assertEquals(duration, result);
      }

      /**
       * Tests retrieving monthly training hours for a trainer that does not exist, expecting zero hours.
       */
      @Test
      public void getMonthlyHours_TrainerDoesNotExist_ReturnsZero() {
            String username = "trainer4";
            int year = 2023;
            int month = 3;

            when(trainerWorkloadService.getTrainerWorkloadByUsername(username))
                    .thenReturn(Optional.empty());

            int result = trainerWorkloadManagementService.getMonthlyHours(username, year, month);

            assertEquals(0, result);
      }
}