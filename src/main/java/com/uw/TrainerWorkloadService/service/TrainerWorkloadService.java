package com.uw.TrainerWorkloadService.service;

import com.uw.TrainerWorkloadService.model.TrainerWorkload;
import com.uw.TrainerWorkloadService.repository.TrainerWorkloadRepository;
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
        return trainerWorkloadRepository.save(trainerWorkload);
    }

    /**
     * Retrieves all TrainerWorkload entities.
     *
     * @return an Optional containing a list of all TrainerWorkload entities
     */
    public Optional<List<TrainerWorkload>> getAllTrainerWorkloads() {
        return Optional.of(trainerWorkloadRepository.findAll());
    }

    /**
     * Retrieves a TrainerWorkload entity by its ID.
     *
     * @param id the ID of the TrainerWorkload entity
     * @return an Optional containing the TrainerWorkload entity if found
     */
    public Optional<TrainerWorkload> getTrainerWorkloadById(long id) {
        return trainerWorkloadRepository.findById(id);
    }

    /**
     * Deletes a TrainerWorkload entity by its ID.
     *
     * @param id the ID of the TrainerWorkload entity to delete
     */
    public void deleteTrainerWorkload(long id) {
        trainerWorkloadRepository.deleteById(id);
    }

    /**
     * Retrieves a TrainerWorkload entity by the trainer's username.
     *
     * @param username the username of the trainer
     * @return an Optional containing the TrainerWorkload entity if found
     */
    public Optional<TrainerWorkload> getTrainerWorkloadByUsername(String username) {
        return trainerWorkloadRepository.findByTrainerUsername(username);
    }
}