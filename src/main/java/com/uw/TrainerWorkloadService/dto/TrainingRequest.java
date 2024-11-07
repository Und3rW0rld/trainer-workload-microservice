package com.uw.TrainerWorkloadService.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Data Transfer Object (DTO) for training requests.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TrainingRequest {
    /**
     * The username of the trainer.
     */
    @NotNull(message = "Trainer username cannot be null")
    private String trainerUsername;

    /**
     * The first name of the trainer.
     */
    @NotNull(message = "Trainer first name cannot be null")
    private String firstName;

    /**
     * The last name of the trainer.
     */
    @NotNull(message = "Trainer last name cannot be null")
    private String lastName;

    /**
     * Indicates whether the trainer is active.
     */
    @NotNull(message = "Trainer active status cannot be null")
    private boolean isActive;

    /**
     * The date of the training session.
     */
      @NotNull(message = "Training date cannot be null")
    private LocalDate trainingDate;

    /**
     * The duration of the training session in hours.
     */
    @Positive(message = "Training duration must be positive")
    private int trainingDuration;

    /**
     * The action type for the training request (e.g., "add" or "delete").
     */
      @NotNull(message = "Action type cannot be null")
    private String actionType;
}