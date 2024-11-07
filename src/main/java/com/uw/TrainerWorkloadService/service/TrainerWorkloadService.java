package com.uw.TrainerWorkloadService.service;

import com.uw.TrainerWorkloadService.model.TrainerWorkload;
import com.uw.TrainerWorkloadService.repository.TrainerWorkloadRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class for managing TrainerWorkload entities.
 * This class handles the business logic for saving, retrieving, and deleting trainer workloads.
 */
@Service
public class TrainerWorkloadService {
    private static final Logger logger = LoggerFactory.getLogger(TrainerWorkloadService.class);
    private final TrainerWorkloadRepository trainerWorkloadRepository;

    /**
     * Constructor for TrainerWorkloadService.
     *
     * @param trainerWorkloadRepository the repository for TrainerWorkload entities
     */
    @Autowired
    public TrainerWorkloadService(TrainerWorkloadRepository trainerWorkloadRepository) {
        this.trainerWorkloadRepository = trainerWorkloadRepository;
    }

    /**
     * Saves a TrainerWorkload entity.
     *
     * @param trainerWorkload the TrainerWorkload entity to save
     * @return the saved TrainerWorkload entity
     */
    public TrainerWorkload saveTrainerWorkload(TrainerWorkload trainerWorkload) {
        logger.info("Saving TrainerWorkload: {}", trainerWorkload);
        return trainerWorkloadRepository.save(trainerWorkload);
    }

    // Method to update a TrainerWorkload by username (if you need custom behavior, this can be adjusted)
    public TrainerWorkload updateTrainerWorkloadByUsername(String username, TrainerWorkload updatedWorkload) {
        Optional<TrainerWorkload> existingWorkloadOpt = getTrainerWorkloadByUsername(username);
        if (existingWorkloadOpt.isPresent()) {
            TrainerWorkload existingWorkload = existingWorkloadOpt.get();
            // Update the existingWorkload with the values from updatedWorkload
            existingWorkload.setId(updatedWorkload.getId());
            existingWorkload.setTrainerUsername(updatedWorkload.getTrainerUsername());
            existingWorkload.setTrainerStatus(updatedWorkload.isTrainerStatus());
            existingWorkload.setYears(updatedWorkload.getYears());
            existingWorkload.setTrainerLastName(updatedWorkload.getTrainerLastName());
            existingWorkload.setTrainerFirstName(updatedWorkload.getTrainerFirstName());

            return saveTrainerWorkload(existingWorkload);
        } else {
            throw new RuntimeException("TrainerWorkload not found for username: " + username);
        }
    }

    /**
     * Retrieves all TrainerWorkload entities.
     *
     * @return an Optional containing a list of all TrainerWorkload entities
     */
    public Optional<List<TrainerWorkload>> getAllTrainerWorkloads() {
        logger.info("Retrieving all TrainerWorkloads");
        return Optional.of(trainerWorkloadRepository.findAll());
    }

    /**
     * Retrieves a TrainerWorkload entity by its ID.
     *
     * @param id the ID of the TrainerWorkload entity
     * @return an Optional containing the TrainerWorkload entity if found
     */
    public Optional<TrainerWorkload> getTrainerWorkloadById(String id) {
        logger.info("Retrieving TrainerWorkload by ID: {}", id);
        return trainerWorkloadRepository.findById(id);
    }

    /**
     * Deletes a TrainerWorkload entity by its ID.
     *
     * @param id the ID of the TrainerWorkload entity to delete
     */
    public void deleteTrainerWorkload(String id) {
        logger.info("Deleting TrainerWorkload by ID: {}", id);
        trainerWorkloadRepository.deleteById(id);
    }

    /**
     * Retrieves a TrainerWorkload entity by the trainer's username.
     *
     * @param username the username of the trainer
     * @return an Optional containing the TrainerWorkload entity if found
     */
    public Optional<TrainerWorkload> getTrainerWorkloadByUsername(String username) {
        logger.info("Retrieving TrainerWorkload by username: {}", username);
        return trainerWorkloadRepository.findByTrainerUsername(username);
    }
}