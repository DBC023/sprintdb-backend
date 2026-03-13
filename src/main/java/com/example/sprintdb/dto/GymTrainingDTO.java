package com.example.sprintdb.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class GymTrainingDTO extends TrainingDTO {
    private Integer series;
    private Integer repetitions;
    private BigDecimal weight;
}