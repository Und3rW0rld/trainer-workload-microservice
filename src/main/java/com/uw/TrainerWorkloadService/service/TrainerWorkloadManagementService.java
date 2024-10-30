package com.uw.TrainerWorkloadService.service;

import com.uw.TrainerWorkloadService.model.Month;
import com.uw.TrainerWorkloadService.model.TrainerWorkload;
import com.uw.TrainerWorkloadService.model.YearSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service class for managing trainer workloads.
 * This class handles the business logic for adding, deleting, and retrieving training hours for trainers.
 */
@Service
public class TrainerWorkloadManagementService {

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
            TrainerWorkload trainerWorkload = getOrCreateTrainerWorkload(username, firstName, lastName, isActive);
            Month monthEnum = Month.fromNumber(month);

            for (YearSummary y : trainerWorkload.getYears()) {
                  if (y.getYear() == year) {
                        y.addHours(monthEnum, duration);
                        trainerWorkloadService.saveTrainerWorkload(trainerWorkload);
                        return "Training added successfully";
                  }
            }

            YearSummary yearSummary = new YearSummary();
            yearSummary.setYear(year);
            yearSummary.addHours(monthEnum, duration);
            trainerWorkload.getYears().add(yearSummary);
            trainerWorkloadService.saveTrainerWorkload(trainerWorkload);

            return "Training added successfully";
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
            TrainerWorkload trainerWorkload = trainerWorkloadService.getTrainerWorkloadByUsername(username)
                    .orElseThrow(() -> new IllegalArgumentException("Training not found"));
            Month monthEnum = Month.fromNumber(month);

            for (YearSummary y : trainerWorkload.getYears()) {
                  if (y.getYear() == year) {
                        y.deleteHours(monthEnum, duration);
                        trainerWorkloadService.saveTrainerWorkload(trainerWorkload);
                        return "Training deleted successfully";
                  }
            }

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
            Optional<TrainerWorkload> existingWorkload = trainerWorkloadService.getTrainerWorkloadByUsername(username);
            if (existingWorkload.isPresent()) {
                  return existingWorkload.get();
            } else {
                  TrainerWorkload newWorkload = new TrainerWorkload();
                  newWorkload.setTrainerUsername(username);
                  newWorkload.setTrainerFirstName(firstName);
                  newWorkload.setTrainerLastName(lastName);
                  newWorkload.setTrainerStatus(isActive);
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
            Optional<TrainerWorkload> trainerWorkloadOptional = trainerWorkloadService.getTrainerWorkloadByUsername(username);

            if (trainerWorkloadOptional.isEmpty()) {
                  return 0;
            }

            TrainerWorkload trainerWorkload = trainerWorkloadOptional.get();
            for (YearSummary y : trainerWorkload.getYears()) {
                  if (y.getYear() == year) {
                        return y.getHours(Month.fromNumber(month));
                  }
            }

            return 0;
      }
}