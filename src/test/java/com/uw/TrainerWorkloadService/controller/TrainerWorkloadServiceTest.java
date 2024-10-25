package com.uw.TrainerWorkloadService.controller;

import com.uw.TrainerWorkloadService.dto.TrainingRequest;
import com.uw.TrainerWorkloadService.model.Month;
import com.uw.TrainerWorkloadService.model.TrainerWorkload;
import com.uw.TrainerWorkloadService.model.YearSummary;
import com.uw.TrainerWorkloadService.service.TrainerWorkloadService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class TrainerWorkloadControllerTest {

      @Mock
      private TrainerWorkloadService trainerWorkloadService;

      @InjectMocks
      private TrainerWorkloadController trainerWorkloadController;

      @BeforeEach
      void setUp() {
            MockitoAnnotations.openMocks(this);
      }

      @Test
      void testTrainingRequest_Add() {
            TrainingRequest request = new TrainingRequest();
            request.setUsername("testUser");
            request.setFirstName("Test");
            request.setLastName("User");
            request.setActive(true);
            request.setTrainingDate(LocalDate.of(2024, 10, 24));
            request.setTrainingDuration(2);
            request.setActionType("add");

            TrainerWorkload trainerWorkload = new TrainerWorkload();
            trainerWorkload.setTrainerUsername("testUser");

            when(trainerWorkloadService.getTrainerWorkloadByUsername(anyString())).thenReturn(Optional.of(trainerWorkload));

            ResponseEntity<?> response = trainerWorkloadController.trainingRequest(request);

            assertEquals(200, response.getStatusCodeValue());
            verify(trainerWorkloadService, times(1)).saveTrainerWorkload(any(TrainerWorkload.class));
      }

      @Test
      void testTrainingRequest_Delete() {
            TrainingRequest request = new TrainingRequest();
            request.setUsername("testUser");
            request.setFirstName("Test");
            request.setLastName("User");
            request.setActive(true);
            request.setTrainingDate(LocalDate.of(2024, 10, 24));
            request.setTrainingDuration(2);
            request.setActionType("delete");

            TrainerWorkload trainerWorkload = new TrainerWorkload();
            trainerWorkload.setTrainerUsername("testUser");
            YearSummary yearSummary = new YearSummary();
            yearSummary.setYear(2024);
            trainerWorkload.getYears().add(yearSummary);

            when(trainerWorkloadService.getTrainerWorkloadByUsername(anyString())).thenReturn(Optional.of(trainerWorkload));

            ResponseEntity<?> response = trainerWorkloadController.trainingRequest(request);

            assertEquals(200, response.getStatusCodeValue());
            verify(trainerWorkloadService, times(1)).saveTrainerWorkload(any(TrainerWorkload.class));
      }

      @Test
      void testGetMonthlyHours() {
            TrainerWorkload trainerWorkload = new TrainerWorkload();
            trainerWorkload.setTrainerUsername("testUser");
            YearSummary yearSummary = new YearSummary();
            yearSummary.setYear(2024);
            yearSummary.addHours(Month.OCTOBER, 10);
            trainerWorkload.getYears().add(yearSummary);

            when(trainerWorkloadService.getTrainerWorkloadByUsername(anyString())).thenReturn(Optional.of(trainerWorkload));

            ResponseEntity<?> response = trainerWorkloadController.getMonthlyHours("testUser", 2024, 10);

            assertEquals(200, response.getStatusCodeValue());
            assertEquals("{\"hours\": 10}", response.getBody());
      }
}
