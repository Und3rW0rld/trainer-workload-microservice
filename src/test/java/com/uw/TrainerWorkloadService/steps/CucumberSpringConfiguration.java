package com.uw.TrainerWorkloadService.steps;

import com.uw.TrainerWorkloadService.TrainerWorkloadServiceApplication;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@CucumberContextConfiguration
@SpringBootTest(classes = TrainerWorkloadServiceApplication.class)
public class CucumberSpringConfiguration {
}