package com.example.sprintdb.repository;

import com.example.sprintdb.entities.TrackTraining;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrackTrainingRepository extends JpaRepository<TrackTraining, Long> {
    // Ejemplo: buscar por distancia específica
    List<TrackTraining> findByAthleteIdAndDistanceMeters(Long athleteId, Integer distance);
}