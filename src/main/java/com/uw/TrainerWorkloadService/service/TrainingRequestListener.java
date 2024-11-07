package com.uw.TrainerWorkloadService.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.uw.TrainerWorkloadService.dto.TrainingRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

/**
 * Service class that listens for training requests from a JMS queue.
 */
@Service
public class TrainingRequestListener {

      Logger logger = LoggerFactory.getLogger(TrainingRequestListener.class);

      private final TrainerWorkloadManagementService trainerWorkloadManagementService;
      private final ObjectMapper objectMapper;
      /**
       * Constructor to inject TrainerWorkloadManagementService.
       *
       * @param trainerWorkloadManagementService the service to manage trainer workload
       */
      @Autowired
      public TrainingRequestListener(TrainerWorkloadManagementService trainerWorkloadManagementService, ObjectMapper objectMapper) {
            this.trainerWorkloadManagementService = trainerWorkloadManagementService;
            this.objectMapper = objectMapper;
      }

      /**
       * Method to receive and process messages from the "training.queue".
       *
       * @param message the message received from the queue
       */
      @JmsListener(destination = "training.queue")
      public void receiveMessage(String message) {
            logger.info("Received message: {}", message);
            try {
                  TrainingRequest trainingRequest = objectMapper.readValue(message, TrainingRequest.class);
                  if (trainingRequest.getActionType().equalsIgnoreCase("add")) {
                        logger.info("Adding training request: {}", trainingRequest);
                        trainerWorkloadManagementService.addTraining(trainingRequest);
                  } else if (trainingRequest.getActionType().equalsIgnoreCase("delete")) {
                        logger.info("Delete training request: {}", trainingRequest);
                        trainerWorkloadManagementService.deleteTraining(trainingRequest);
                  } else {
                        logger.error("Invalid action type: {}", trainingRequest.getActionType());
                  }
            } catch (JsonMappingException e) {
                  logger.error("Error mapping message to TrainingRequest: {}", e.getMessage());
            } catch (JsonProcessingException e) {
                  logger.error("Error processing message: {}", e.getMessage());
            }
      }
}