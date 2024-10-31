package com.uw.TrainerWorkloadService.dto;

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
    private String trainerUsername;

    /**
     * The first name of the trainer.
     */
    private String firstName;

    /**
     * The last name of the trainer.
     */
    private String lastName;

    /**
     * Indicates whether the trainer is active.
     */
    private boolean isActive;

    /**
     * The date of the training session.
     */
    private LocalDate trainingDate;

    /**
     * The duration of the training session in hours.
     */
    private int trainingDuration;

    /**
     * The action type for the training request (e.g., "add" or "delete").
     */
    private String actionType;
}