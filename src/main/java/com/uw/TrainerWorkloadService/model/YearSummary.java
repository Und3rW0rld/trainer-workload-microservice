package com.uw.TrainerWorkloadService.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document
public class YearSummary {

      @Id
      private String id; // En MongoDB el id puede ser de tipo String y se generará automáticamente si no se especifica.

      private int year;

      private Map<Month, Integer> monthlyHours = new HashMap<>();

      public YearSummary(int year) {
            this.year = year;
      }

      public void addHours(Month month, int hours) {
            monthlyHours.put(month, monthlyHours.getOrDefault(month, 0) + hours);
      }

      public void deleteHours(Month month, int hours) {
            monthlyHours.put(month, Math.max(0, monthlyHours.getOrDefault(month, 0) - hours));
      }

      public int getHours(Month month) {
            return monthlyHours.getOrDefault(month, 0);
      }
}
