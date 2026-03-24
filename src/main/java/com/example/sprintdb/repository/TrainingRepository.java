package com.example.sprintdb.repository;

import com.example.sprintdb.entities.Training;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TrainingRepository extends JpaRepository<Training, Long> {
    // Search trainings between two dates
    List<Training> findByDateTimeBetween(LocalDateTime start, LocalDateTime end);

    // View the workouts of a specific athlete ordered by date
    List<Training> findByAthleteIdOrderByDateTimeDesc(Long athleteId);

    //Quick search endponint
    @Query("SELECT DISTINCT t.description FROM Training t WHERE LOWER(t.description) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<String> findUniqueDescriptions(@Param("query") String query);


}