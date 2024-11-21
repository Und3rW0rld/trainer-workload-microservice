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


  Scenario: Do not add hours to trainer workload if request data is invalid
    Given an existing trainer workload for trainer with username "Pedro"
    When an add hours request with invalid data is processed
    Then the trainer workload should not be updated in the database


  Scenario: Handle invalid request data for workload update
    Given an invalid training request with missing or incorrect fields
    When the bad request is processed
    Then the system should return an error response
    And an error message should indicate the reason for the invalid request
