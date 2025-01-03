package com.uw.TrainerWorkloadService.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Document(collection = "trainerWorkload")
public class TrainerWorkload {

    @Id
    private String id;

    @Indexed
    private String trainerUsername;
    @Indexed
    private String trainerFirstName;
    @Indexed
    private String trainerLastName;
    private boolean trainerStatus;

    private List<YearSummary> years = new ArrayList<>();

}