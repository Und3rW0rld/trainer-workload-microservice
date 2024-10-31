package com.uw.TrainerWorkloadService.controller;

import com.uw.TrainerWorkloadService.dto.TrainingRequest;
import com.uw.TrainerWorkloadService.service.TrainerWorkloadManagementService;
import com.uw.TrainerWorkloadService.service.TrainerWorkloadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing trainer workloads.
 */
@RestController
@RequestMapping("/trainer-workload")
public class TrainerWorkloadController {

      private final TrainerWorkloadManagementService trainerWorkloadManagementService;
      private final TrainerWorkloadService trainerWorkloadService;

      /**
       * Constructor for TrainerWorkloadController.
       *
       * @param trainerWorkloadManagementService the service for managing trainer workloads
       * @param trainerWorkloadService the service for retrieving trainer workloads
       */
      @Autowired
      public TrainerWorkloadController(TrainerWorkloadManagementService trainerWorkloadManagementService, TrainerWorkloadService trainerWorkloadService) {
            this.trainerWorkloadManagementService = trainerWorkloadManagementService;
            this.trainerWorkloadService = trainerWorkloadService;
      }

      /**
       * Handles training requests to add or delete training sessions.
       *
       * @param trainingRequest the training request object containing details of the training session
       * @return a ResponseEntity with the result of the operation
       */
      @PostMapping("/training-request")
      public ResponseEntity<?> trainingRequest(@RequestBody TrainingRequest trainingRequest) {
            // Validate the training request object to ensure that it is not null
            if (trainingRequest == null) {
                  return ResponseEntity.badRequest().body("{\"message\": \"Invalid training request\"}");
            }
            return trainerWorkloadManagementService.processRequest(trainingRequest);

      }

      /**
       * Retrieves the monthly training hours for a specific trainer.
       *
       * @param username the username of the trainer
       * @param year the year for which to retrieve the hours
       * @param month the month for which to retrieve the hours
       * @return a ResponseEntity with the number of hours or an error message if the trainer is not found
       */
      @GetMapping("/{username}/{year}/{month}")
      public ResponseEntity<?> getMonthlyHours(
              @PathVariable(name = "username") String username,
              @PathVariable(name = "year") int year,
              @PathVariable(name = "month") int month) {

            if (trainerWorkloadService.getTrainerWorkloadByUsername(username).isEmpty()) {
                  // 404 Not Found if the trainer does not exist. message: "User not found"
                  return new ResponseEntity<>("{\"message\": \"User not found\"}", HttpStatus.NOT_FOUND);
            }

            int hours = trainerWorkloadManagementService.getMonthlyHours(username, year, month);
            return ResponseEntity.ok().body("{\"hours\": " + hours + "}");
      }

}