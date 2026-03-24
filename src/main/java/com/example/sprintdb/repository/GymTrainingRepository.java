package com.example.sprintdb.repository;

import com.example.sprintdb.entities.GymTraining;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GymTrainingRepository extends JpaRepository<GymTraining, Long> {
    // buscar entrenamientos con un peso mayor a X.
    List<GymTraining> findByAthleteIdAndWeightGreaterThan(Long athleteId, java.math.BigDecimal weight);
}
