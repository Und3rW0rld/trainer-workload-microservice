package com.uw.TrainerWorkloadService.service;

import com.uw.TrainerWorkloadService.model.TrainerWorkload;
import com.uw.TrainerWorkloadService.repository.TrainerWorkloadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Month;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TrainerWorkloadService {
    private final TrainerWorkloadRepository trainerWorkloadRepository;

    @Autowired
    public TrainerWorkloadService(TrainerWorkloadRepository trainerWorkloadRepository) {
        this.trainerWorkloadRepository = trainerWorkloadRepository;
    }

    public TrainerWorkload saveTrainerWorkload(TrainerWorkload trainerWorkload) {
        return trainerWorkloadRepository.save(trainerWorkload);
    }

    public Optional<List<TrainerWorkload>> getAllTrainerWorkloads() {
        return Optional.of(trainerWorkloadRepository.findAll());
    }

    public Optional<TrainerWorkload> getTrainerWorkloadById(long id) {
        return trainerWorkloadRepository.findById(id);
    }

    public void deleteTrainerWorkload(long id) {
        trainerWorkloadRepository.deleteById(id);
    }

    public Optional<TrainerWorkload> getTrainerWorkloadByUsername(String username) {
        return trainerWorkloadRepository.findByTrainerUsername(username);
    }


}
