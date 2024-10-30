# Trainer Workload Service

This project provides an API for managing trainer workloads, allowing trainers to track and manage training hours monthly and yearly. The service includes endpoints for adding and deleting training sessions, retrieving monthly training hours, and handling trainers’ workloads efficiently.

## Features

- **Add Training Hours**: Allows trainers to add hours for a specific month and year.
- **Delete Training Hours**: Removes training hours for a specific session.
- **Monthly Hours Retrieval**: Retrieves total hours worked by a trainer in a specified month.
- **Error Handling**: Provides validation and error messages for incorrect requests.

## Project Structure

The project follows a layered architecture with a focus on service and controller separation. Key components include:

- **Controller Layer**: Handles API endpoints and request validation.
- **Service Layer**: Contains business logic for managing training workloads.
- **Model Layer**: Defines data models like `TrainerWorkload`, `YearSummary`, and `Month`.

## Setup

### Prerequisites
- Java 17
- Maven

## API Endpoints

### 1. Add or Delete Training Hours
   - **POST** `/trainer-workload/training-request`
   - **Request Body**: JSON object containing `username`, `actionType` (add/delete), and other training details.

### 2. Get Monthly Hours
   - **GET** `/trainer-workload/{username}/{year}/{month}`
   - **Path Variables**: 
     - `username`: Trainer’s unique ID
     - `year`: Year of the workload data
     - `month`: Month of the workload data (numeric)

## Example Request Payload

### Add Training Request
```json
{
  "username": "trainer1",
  "firstName": "John",
  "lastName": "Doe",
  "actionType": "add",
  "trainingDate": "2024-10-24",
  "trainingDuration": 2,
  "active": true
}
```

### Delete Training Request
```json
{
  "username": "trainer1",
  "actionType": "delete",
  "trainingDate": "2024-10-24",
  "trainingDuration": 2
}
```
