Feature: Trainer Workload Service Integration Test

  Scenario: Create or update trainer workload based on training request
    Given a new training request from Microservice A for trainer with username "Pedro"
    When the request is processed by Microservice B
    Then the trainer workload should be created or updated in the database
