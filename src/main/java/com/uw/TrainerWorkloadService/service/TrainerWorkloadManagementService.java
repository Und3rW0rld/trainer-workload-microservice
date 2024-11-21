package com.uw.TrainerWorkloadService.service;

import com.uw.TrainerWorkloadService.dto.TrainingRequest;
import com.uw.TrainerWorkloadService.model.Month;
import com.uw.TrainerWorkloadService.model.TrainerWorkload;
import com.uw.TrainerWorkloadService.model.YearSummary;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service class for managing trainer workloads.
 * This class handles the business logic for adding, deleting, and retrieving training hours for trainers.
 */
@Service
public class TrainerWorkloadManagementService {

      private static final Logger logger = LoggerFactory.getLogger(TrainerWorkloadManagementService.class);
      private final TrainerWorkloadService trainerWorkloadService;

      /**
       * Constructor for TrainerWorkloadManagementService.
       *
       * @param trainerWorkloadService the service for retrieving and saving trainer workloads
       */
      @Autowired
      public TrainerWorkloadManagementService(TrainerWorkloadService trainerWorkloadService) {
            this.trainerWorkloadService = trainerWorkloadService;
      }

      /**
       * Adds training hours to a trainer's workload.
       * If the trainer or year summary does not exist, they are created.
       *
       * @param username the username of the trainer
       * @param firstName the first name of the trainer
       * @param lastName the last name of the trainer
       * @param isActive the status indicating whether the trainer is active
       * @param year the year of the training session
       * @param month the month of the training session
       * @param duration the duration of the training session in hours
       * @return a message indicating the result of the operation
       */
      public String addTraining(String username, String firstName, String lastName, boolean isActive,
                                int year, int month, int duration) {
            logger.info("Adding training for trainer: {}, year: {}, month: {}, duration: {}", username, year, month, duration);
            TrainerWorkload trainerWorkload = getOrCreateTrainerWorkload(username, firstName, lastName, isActive);
            Month monthEnum = Month.fromNumber(month);

            for (YearSummary y : trainerWorkload.getYears()) {
                  if (y.getYear() == year) {
                        y.addHours(monthEnum, duration);
                        trainerWorkloadService.saveTrainerWorkload(trainerWorkload);
                        logger.info("Training added successfully for trainer: {}", username);
                        return "Training added successfully";
                  }
            }

            YearSummary yearSummary = new YearSummary();
            yearSummary.setYear(year);
            yearSummary.addHours(monthEnum, duration);
            trainerWorkload.getYears().add(yearSummary);
            trainerWorkloadService.saveTrainerWorkload(trainerWorkload);
            logger.info("Training added successfully for trainer: {}", username);
            return "Training added successfully";
      }

      /**
       * Adds training hours to a trainer's workload.
       * If the trainer or year summary does not exist, they are created.
       *
       * @param trainingRequest the training request data transfer object
       * @return a message indicating the result of the operation
       */
      public String addTraining(TrainingRequest trainingRequest) {
            logger.info("Adding training for trainer: {}, trainingRequest: {}", trainingRequest.getTrainerUsername(), trainingRequest);

            //Validations

            if (trainingRequest.getTrainerUsername() == null) {
                  logger.error("Invalid trainer username: {}", (Object) null);
                  throw new IllegalArgumentException("Trainer username must not be null");
            }

            if (trainingRequest.getTrainingDate() == null) {
                  logger.error("Invalid training date: {}", (Object) null);
                  throw new IllegalArgumentException("Training date must not be null");
            }

            if (trainingRequest.getTrainingDuration() <= 0) {
                  logger.error("Invalid training duration: {}", trainingRequest.getTrainingDuration());
                  throw new IllegalArgumentException("Training duration must be greater than 0");
            }

            int month = trainingRequest.getTrainingDate().getMonthValue();

            TrainerWorkload trainerWorkload = getOrCreateTrainerWorkload(trainingRequest.getTrainerUsername(), trainingRequest.getFirstName(), trainingRequest.getLastName(), trainingRequest.isActive());
            Month monthEnum = Month.fromNumber(month);

            for (YearSummary y : trainerWorkload.getYears()) {
                  if (y.getYear() == trainingRequest.getTrainingDate().getYear()) {
                        y.addHours(monthEnum, trainingRequest.getTrainingDuration());
                        trainerWorkloadService.saveTrainerWorkload(trainerWorkload);
                        logger.info("Training workload added successfully for trainer: {}", trainingRequest.getTrainerUsername());
                        return "Training workload added successfully";
                  }
            }

            YearSummary yearSummary = new YearSummary();
            yearSummary.setYear(trainingRequest.getTrainingDate().getYear());
            yearSummary.addHours(monthEnum, trainingRequest.getTrainingDuration());
            trainerWorkload.getYears().add(yearSummary);
            trainerWorkloadService.saveTrainerWorkload(trainerWorkload);
            logger.info("Training workload added successfully for trainer: {}", trainingRequest.getTrainerUsername());
            return "Training workload added successfully";
      }


      /**
       * Deletes training hours from a trainer's workload.
       * If the training session is not found, an IllegalArgumentException is thrown.
       *
       * @param username the username of the trainer
       * @param year the year of the training session
       * @param month the month of the training session
       * @param duration the duration of the training session in hours
       * @return a message indicating the result of the operation
       * @throws IllegalArgumentException if the training session is not found
       */
      public String deleteTraining(String username, int year, int month, int duration) {
            logger.info("Deleting training for trainer: {}, year: {}, month: {}, duration: {}", username, year, month, duration);
            TrainerWorkload trainerWorkload = trainerWorkloadService.getTrainerWorkloadByUsername(username)
                    .orElseThrow(() -> new IllegalArgumentException("Training not found"));
            Month monthEnum = Month.fromNumber(month);

            System.out.println("trainerWorkload: " + trainerWorkload);

            for (YearSummary y : trainerWorkload.getYears()) {
                  if (y.getYear() == year) {
                        y.deleteHours(monthEnum, duration);
                        trainerWorkloadService.saveTrainerWorkload(trainerWorkload);
                        logger.info("Training workload deleted successfully for trainer: {}", username);
                        return "Training workload deleted successfully";
                  }
            }

            logger.error("Training not found for trainer: {}, year: {}, month: {}, duration: {}", username, year, month, duration);
            throw new IllegalArgumentException("Training not found");
      }

      /**
       * Deletes training hours from a trainer's workload.
       * If the training session is not found, an IllegalArgumentException is thrown.
       *
       * @param trainingRequest the training request data transfer object
       * @return a message indicating the result of the operation
       * @throws IllegalArgumentException if the training session is not found
       */
      public String deleteTraining(TrainingRequest trainingRequest) {
            logger.info("Deleting training for trainer: {}, trainingRequest: {}", trainingRequest.getTrainerUsername(), trainingRequest);
            TrainerWorkload trainerWorkload = trainerWorkloadService.getTrainerWorkloadByUsername(trainingRequest.getTrainerUsername())
                    .orElseThrow(() -> new IllegalArgumentException("Training not found"));
            Month monthEnum = Month.fromNumber(trainingRequest.getTrainingDate().getMonthValue());

            for (YearSummary y : trainerWorkload.getYears()) {
                  if (y.getYear() == trainingRequest.getTrainingDate().getYear()) {
                        y.deleteHours(monthEnum, trainingRequest.getTrainingDuration());
                        trainerWorkloadService.saveTrainerWorkload(trainerWorkload);
                        logger.info("Training workload deleted successfully for trainer: {}", trainingRequest.getTrainerUsername());
                        return "Training workload deleted successfully";
                  }
            }

            logger.error("Training not found for trainer: {}, trainingRequest: {}", trainingRequest.getTrainerUsername(), trainingRequest);
            throw new IllegalArgumentException("Training not found");
      }

      /**
       * Retrieves or creates a TrainerWorkload object.
       * If the trainer does not exist, a new TrainerWorkload object is created.
       *
       * @param username the username of the trainer
       * @param firstName the first name of the trainer
       * @param lastName the last name of the trainer
       * @param isActive the status indicating whether the trainer is active
       * @return the TrainerWorkload object
       */
      private TrainerWorkload getOrCreateTrainerWorkload(String username, String firstName, String lastName, boolean isActive) {
            logger.info("Retrieving or creating TrainerWorkload for trainer: {}", username);
            Optional<TrainerWorkload> existingWorkload = trainerWorkloadService.getTrainerWorkloadByUsername(username);
            if (existingWorkload.isPresent()) {
                  return existingWorkload.get();
            } else {
                  TrainerWorkload newWorkload = new TrainerWorkload();
                  newWorkload.setTrainerUsername(username);
                  newWorkload.setTrainerFirstName(firstName);
                  newWorkload.setTrainerLastName(lastName);
                  newWorkload.setTrainerStatus(isActive);
                  logger.info("Created new TrainerWorkload for trainer: {}", username);
                  return newWorkload;
            }
      }

      /**
       * Retrieves the monthly training hours for a specific trainer.
       * If the trainer or year summary does not exist, 0 is returned.
       *
       * @param username the username of the trainer
       * @param year the year of the training session
       * @param month the month of the training session
       * @return the number of training hours for the specified month and year
       */
      public int getMonthlyHours(String username, int year, int month) {
            logger.info("Retrieving monthly hours for trainer: {}, year: {}, month: {}", username, year, month);
            Optional<TrainerWorkload> trainerWorkloadOptional = trainerWorkloadService.getTrainerWorkloadByUsername(username);

            if (trainerWorkloadOptional.isEmpty()) {
                  logger.warn("TrainerWorkload not found for trainer: {}", username);
                  return 0;
            }

            TrainerWorkload trainerWorkload = trainerWorkloadOptional.get();
            for (YearSummary y : trainerWorkload.getYears()) {
                  if (y.getYear() == year) {
                        return y.getHours(Month.fromNumber(month));
                  }
            }

            logger.warn("YearSummary not found for trainer: {}, year: {}", username, year);
            return 0;
      }

      /**
       * Processes a training request and returns a response entity.
       * The request is validated and the appropriate method is called based on the action type.
       *
       * @param trainingRequest the training request data transfer object
       * @return a response entity containing a message indicating the result of the operation
       */
      public ResponseEntity<String> processRequest(@Valid TrainingRequest trainingRequest) {
            logger.info("Processing training request for trainer: {}, actionType: {}", trainingRequest.getTrainerUsername(), trainingRequest.getActionType());
            if( trainingRequest.getTrainerUsername() == null || trainingRequest.getTrainerUsername().isBlank() ) {
                  logger.error("Invalid trainer username: ");
                  return ResponseEntity.badRequest().body("{\"message\": \"Trainer username must not be blank\"}");
            }
            if ( trainingRequest.getTrainingDuration() < 0 ){
                  throw new IllegalArgumentException("Invalid training duration: training duration must be greater than 0");
            }
            try {
                  // Validate the action type and call the appropriate method in the TrainerWorkloadManagementService
                  String message;
                  if ("add".equalsIgnoreCase(trainingRequest.getActionType())) {
                        message = addTraining(
                                trainingRequest.getTrainerUsername(),
                                trainingRequest.getFirstName(),
                                trainingRequest.getLastName(),
                                trainingRequest.isActive(),
                                trainingRequest.getTrainingDate().getYear(),
                                trainingRequest.getTrainingDate().getMonthValue(),
                                trainingRequest.getTrainingDuration()
                        );
                  } else if ("delete".equalsIgnoreCase(trainingRequest.getActionType())) {
                        message = deleteTraining(
                                trainingRequest.getTrainerUsername(),
                                trainingRequest.getTrainingDate().getYear(),
                                trainingRequest.getTrainingDate().getMonthValue(),
                                trainingRequest.getTrainingDuration()
                        );
                  } else {
                        logger.error("Invalid action type: {}", trainingRequest.getActionType());
                        return ResponseEntity.badRequest().body("{\"message\": \"Invalid action type\"}");
                  }
                  logger.info("Training request processed successfully for trainer: {}", trainingRequest.getTrainerUsername());
                  return ResponseEntity.ok().body("{\"message\": \"" + message + "\"}");
            } catch (IllegalArgumentException e) {
                  logger.error("Error processing training request for trainer: {}, error: {}", trainingRequest.getTrainerUsername(), e.getMessage());
                  return ResponseEntity.badRequest().body("{\"message\": \"" + e.getMessage() + "\"}");
            }
      }
}