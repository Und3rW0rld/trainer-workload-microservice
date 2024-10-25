package com.uw.TrainerWorkloadService.controller;

import com.uw.TrainerWorkloadService.dto.TrainingRequest;
import com.uw.TrainerWorkloadService.model.Month;
import com.uw.TrainerWorkloadService.model.TrainerWorkload;
import com.uw.TrainerWorkloadService.model.YearSummary;
import com.uw.TrainerWorkloadService.service.TrainerWorkloadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/trainer-workload")
public class TrainerWorkloadController {

      private final TrainerWorkloadService trainerWorkloadService;

      @Autowired
      public TrainerWorkloadController ( TrainerWorkloadService trainerWorkloadService ) {
            this.trainerWorkloadService = trainerWorkloadService;
      }

      @PostMapping("/training-request")
      public ResponseEntity<?> trainingRequest(@RequestBody TrainingRequest trainingRequest) {
            if (trainingRequest == null) {
                  return ResponseEntity.badRequest().body("{\"message\": \"Invalid training request\"}");
            }
            TrainerWorkload trainerWorkload;
            if (trainerWorkloadService.getTrainerWorkloadByUsername(trainingRequest.getUsername()).isPresent()) {
                  trainerWorkload = trainerWorkloadService.getTrainerWorkloadByUsername(trainingRequest.getUsername()).get();
            } else {
                  trainerWorkload = new TrainerWorkload();
                  trainerWorkload.setTrainerUsername(trainingRequest.getUsername());
                  trainerWorkload.setTrainerFirstName(trainingRequest.getFirstName());
                  trainerWorkload.setTrainerLastName(trainingRequest.getLastName());
                  trainerWorkload.setTrainerStatus(trainingRequest.isActive());
            }

            int year = trainingRequest.getTrainingDate().getYear();
            Month month = Month.fromNumber(trainingRequest.getTrainingDate().getMonthValue());

            if (trainingRequest.getActionType().equalsIgnoreCase("add")) {
                  // Add training
                  for (YearSummary y : trainerWorkload.getYears()) {
                        if (y.getYear() == year) {
                              y.addHours(month, trainingRequest.getTrainingDuration());
                              trainerWorkloadService.saveTrainerWorkload(trainerWorkload);
                              return ResponseEntity.ok().body("{\"message\": \"Training added successfully\"}");
                        }
                  }
                  YearSummary yearSummary = new YearSummary();
                  yearSummary.setYear(year);
                  yearSummary.addHours(month, trainingRequest.getTrainingDuration());
                  trainerWorkload.getYears().add(yearSummary);
                  trainerWorkloadService.saveTrainerWorkload(trainerWorkload);
                  return ResponseEntity.ok().body("{\"message\": \"Training added successfully\"}");

            } else if (trainingRequest.getActionType().equalsIgnoreCase("delete")) {
                  // Delete training
                  for (YearSummary y : trainerWorkload.getYears()) {
                        if (y.getYear() == year) {
                              y.deleteHours(month, trainingRequest.getTrainingDuration());
                              trainerWorkloadService.saveTrainerWorkload(trainerWorkload);
                              return ResponseEntity.ok().body("{\"message\": \"Training deleted successfully\"}");
                        }
                  }
                  return ResponseEntity.badRequest().body("{\"message\": \"Training not found\"}");
            } else {
                  return ResponseEntity.badRequest().body("{\"message\": \"Invalid action type\"}");
            }
      }

      @GetMapping("/{username}/{year}/{month}")
      public ResponseEntity<?> getMonthlyHours(
              @PathVariable(name = "username") String username,
              @PathVariable(name = "year") int year,
              @PathVariable(name = "month") int month) {
            Optional<TrainerWorkload> trainerWorkloadOptional = trainerWorkloadService.getTrainerWorkloadByUsername(username);
            if (trainerWorkloadOptional.isEmpty()) {
                  return ResponseEntity.ok().body("{\"hours\": 0}");
            }
            TrainerWorkload trainerWorkload = trainerWorkloadOptional.get();
            for (YearSummary y : trainerWorkload.getYears()) {
                  if (y.getYear() == year) {
                        int hours = y.getHours(Month.fromNumber(month));
                        return ResponseEntity.ok().body("{\"hours\": " + hours + "}");
                  }
            }
            return ResponseEntity.ok().body("{\"hours\": 0}");
      }

}
