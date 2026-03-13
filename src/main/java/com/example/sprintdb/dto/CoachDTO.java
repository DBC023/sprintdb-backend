package com.example.sprintdb.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CoachDTO {
    private Long id;
    private String name;
    private String speciality;
    private List<AthleteSummaryDTO> athletes; // Solo los nombres para no sobrecargar
}

