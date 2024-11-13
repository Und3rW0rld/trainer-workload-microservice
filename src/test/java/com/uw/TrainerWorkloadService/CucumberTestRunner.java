package com.uw.TrainerWorkloadService;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",
        glue = "com.uw.TrainerWorkloadService.steps"
)
public class CucumberTestRunner {
}
