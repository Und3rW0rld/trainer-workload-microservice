Feature: Trainer Workload Service Validation Test

  Scenario: Invalid training request with empty username
    Given an invalid training request with empty username
    When the invalid request is processed
    Then the trainer workload should not be created in the database