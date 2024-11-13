Feature: Trainer Workload Service Component Test

  Scenario: Create new trainer workload if it does not if it does not exist
    Given a new training request for trainer with username "Pedro"
    When the request is processed
    Then a new trainer workload should be created in the database

  Scenario: Update an existing trainer workload by adding hours
    Given an existing trainer workload for trainer with username "Pedro"
    When an add hours request is processed
    Then the trainer workload should be updated in the database

  Scenario: Update an existing trainer workload by removing hours
    Given an existing trainer workload for trainer with username "Pedro"
    When a remove hours request is processed
    Then the trainer workload should be updated in the database