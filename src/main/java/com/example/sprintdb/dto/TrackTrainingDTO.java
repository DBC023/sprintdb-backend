package com.example.sprintdb.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TrackTrainingDTO extends TrainingDTO {
    private Integer series;
    private Integer repetitions;
    private Integer distanceMeters;
    private BigDecimal timeSeconds;
}