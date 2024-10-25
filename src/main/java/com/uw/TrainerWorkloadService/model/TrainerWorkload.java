package com.uw.TrainerWorkloadService.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class TrainerWorkload {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String trainerUsername;
    private String trainerFirstName;
    private String trainerLastName;
    private boolean trainerStatus;

    @OneToMany (cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true, targetEntity = YearSummary.class)
    private List<YearSummary> years = new ArrayList<>();


}