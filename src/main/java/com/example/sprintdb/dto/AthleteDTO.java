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
public class AthleteDTO {
    private Long id;
    private String name;
    private String lastName;
    private Integer age;
    private BigDecimal weight;
    private String coachName;
    private Long CoachId;// En lugar del objeto Coach completo
}
