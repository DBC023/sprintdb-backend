package com.example.sprintdb.entities;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class GymTraining extends Training {
    private Integer series;
    private Integer repetitions;
    private BigDecimal weight;
}
