package com.example.sprintdb.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StatsDTO {
    private Long id;

    private Integer rpe;            //Rango 1-15
    private String observations;
    private Long trainingId;

}
