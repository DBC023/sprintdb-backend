package com.example.sprintdb.entities;

import jakarta.persistence.*;
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
@Table(name = "stats")
public class Stats {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer rpe; // escala 1-10

    @Column(length = 500)
    private String observations;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "training_id", unique = true)
    private Training training;


}
