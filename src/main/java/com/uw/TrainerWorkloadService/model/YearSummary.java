package com.uw.TrainerWorkloadService.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class YearSummary {
      @Id
      @GeneratedValue(strategy = GenerationType.IDENTITY)
      @Column(name = "year_summary_id")
      private long id;
      @Column(name = "year_value")
      private int year;

      @ElementCollection(fetch = FetchType.EAGER) // Para persistir un mapa en JPA
      @CollectionTable(name = "monthly_hours", joinColumns = @JoinColumn(name = "year_summary_id"))
      @MapKeyEnumerated(EnumType.STRING) // Para almacenar el enum como String
      Map<Month, Integer> monthlyHours = new HashMap<>();

      public YearSummary(int year) {
            this.year = year;
      }

      public void addHours(Month month, int hours) {
            monthlyHours.put(month, monthlyHours.getOrDefault(month, 0) + hours);
      }

      public void deleteHours ( Month month, int hours ){
            monthlyHours.put(month, Math.max(0, monthlyHours.getOrDefault(month, 0) - hours));
      }

      public int getHours(Month month) {
            return monthlyHours.getOrDefault(month, 0);
      }
}
