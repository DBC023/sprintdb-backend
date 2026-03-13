package com.example.sprintdb.repository;

import com.example.sprintdb.entities.Training;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TrainingRepository extends JpaRepository<Training, Long> {
    // Buscar entrenamientos entre dos fechas
    List<Training> findByDateTimeBetween(LocalDateTime start, LocalDateTime end);

    // Ver los entrenamientos de un atleta específico ordenados por fecha
    List<Training> findByAthleteIdOrderByDateTimeDesc(Long athleteId);
}