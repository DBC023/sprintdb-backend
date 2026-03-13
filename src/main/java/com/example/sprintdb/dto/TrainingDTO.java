package com.example.sprintdb.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TrainingDTO {
    private Long id;
    private LocalDateTime dateTime;
    private String description;
    private Long athleteId;
    private List<StatsDTO> stats; // Lista de resultados de la sesión
    private Boolean completed;


}